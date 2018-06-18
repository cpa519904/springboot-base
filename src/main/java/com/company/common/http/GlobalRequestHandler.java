package com.company.common.http;

import com.company.common.annotations.UncheckToken;
import com.company.common.exception.ServiceException;
import com.company.common.exception.SystemException;
import com.company.common.tools.Constants;
import com.company.common.exception.ExceptionCode;
import com.company.common.tools.JsonUtil;
import com.company.common.tools.ThreadLocalUtil;
import com.company.common.tools.Utils;
import com.company.common.tools.redis.RedisHelper;
import com.company.pojo.entity.CurrentUser;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

@Configuration
public class GlobalRequestHandler extends WebMvcConfigurerAdapter {
    @Value("${rememberMe.time}")
    private String rememberMeTime;
    @Value("${secretKey}")
    private String secretKey;

    @Autowired
    private RedisHelper redisHelper;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor());
    }

    public HandlerInterceptorAdapter requestInterceptor() {
        return new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

                interceptorHandler(request, handler);

                return true;
            }
        };
    }

    private void interceptorHandler(HttpServletRequest request, Object handler) {
        //校验参数
//        checkParam(request);
        //校验token（token放在cookie和redis里，这是针对H5，如果是app可以放到response头里，用request头接收）
        checkTokenAndRole(request, handler);
        //语言国际化
//        locale(request);
        //验签
//        verifySign(request);
        //防重放
//        antiReplay();
    }

    private void checkParam(HttpServletRequest request) {
        String appName = request.getHeader(Constants.APP_NAME);
        String appVersion = request.getHeader(Constants.APP_VERSION);
        String sign = request.getHeader(Constants.SIGN);
        if (StringUtils.isEmpty(appName) || StringUtils.isEmpty(appVersion) || StringUtils.isEmpty(sign)) {
            throw new ServiceException(ExceptionCode.HANDLER_PARAM_ERROR.getCode(), "handler param is null!");
        }

        //设置请求信息
        MDC.put(Constants.APP_NAME, appName);
        MDC.put(Constants.APP_VERSION, appVersion);
        MDC.put("requestId", UUID.randomUUID().toString());
    }

    private void checkTokenAndRole(HttpServletRequest request, Object handle) {
        if (((HandlerMethod) handle).getMethod().isAnnotationPresent(UncheckToken.class)) {
            return;
        }

        String token = Utils.getCookieByName(request.getCookies(), Constants.TOKEN);
        if (StringUtils.isEmpty(token)) {
            throw new ServiceException(ExceptionCode.NEED_LOGIN.getCode(), "token缺失");
        }

        //此处是用token来找用户信息，不是极端情况不会出现重复问题，但是达到数十万级的QPS时候，UUID还是会出现重复现象，可以token+用户名形式做key
        String userInfo = redisHelper.readValue(token);
        if (StringUtils.isEmpty(userInfo)) {
            throw new ServiceException(ExceptionCode.NEED_LOGIN.getCode(), "token过期");
        } else {
            //刷新有效期
            redisHelper.writeString(token, userInfo, Long.parseLong(rememberMeTime));
            //缓存当前用户
            CurrentUser currentUser = JsonUtil.String2Object(userInfo, CurrentUser.class);
            ThreadLocalUtil.set(currentUser);

            //鉴权, 没写@RolesAllowed为任何权限均可访问
            String userRole = currentUser.getRole();
            RolesAllowed rolesAllowed = ((HandlerMethod) handle).getMethod().getAnnotation(RolesAllowed.class);
            if (rolesAllowed != null && rolesAllowed.value().length != 0) {
                if (!Arrays.asList(rolesAllowed.value()).contains(userRole)) {
                    throw new ServiceException(ExceptionCode.AUTH_ERROR.getCode(), "无权访问");
                }
            }
        }
    }

    private void locale(HttpServletRequest request) {
        String lang = request.getHeader(Constants.LANG);
        LocaleContextHolder.setLocale(StringUtils.isEmpty(lang) ? Locale.SIMPLIFIED_CHINESE : Locale.forLanguageTag(lang));
    }

    private void verifySign(HttpServletRequest request) {
        String paramData = JsonUtil.Object2String(request.getParameterMap());
        String sign = request.getHeader(Constants.SIGN);
        if (!Utils.checkSign(paramData, sign, secretKey)) {
            throw new ServiceException(ExceptionCode.SIGN_ERROR.getCode(), "签名错误");
        }
    }

    private void antiReplay() {
        //timestamp + uuid
    }
}