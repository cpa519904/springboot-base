package com.company.common.base.handler;

import com.company.common.annotations.OpenInterface;
import com.company.common.annotations.UncheckToken;
import com.company.common.exception.SystemException;
import com.company.common.tools.Utils;
import com.company.common.tools.Constants;
import com.company.common.exception.ExceptionCode;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class GlobalRequestHandler extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor());
    }

    public HandlerInterceptorAdapter requestInterceptor() {
        return new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                //OpenInterface注释的接口不拦截
                if (((HandlerMethod) handler).getMethod().isAnnotationPresent(OpenInterface.class)) {
                    return true;
                }

                interceptorHandler(request, handler);

                return true;
            }
        };
    }

    private void interceptorHandler(HttpServletRequest request, Object handler) {
        //获取请求信息
        String appName = request.getHeader(Constants.APP_NAME);
        String appVersion = request.getHeader(Constants.APP_VERSION);
        //String lang = request.getHeader(Constants.LANG);
        if (StringUtils.isEmpty(appName) || StringUtils.isEmpty(appVersion)) {
            throw new SystemException(ExceptionCode.HANDLER_PARAM_ERROR.getCode(), "handler param is null!");
        }

        //清空线程缓存
        MDC.clear();

        //设置请求信息
        MDC.put(Constants.APP_NAME, appName);
        MDC.put(Constants.APP_VERSION, appVersion);
        //语言国际化，有需要开启
        //LocaleContextHolder.setLocale(StringUtils.isEmpty(lang) ? Constants.DEFAULT_LANG : Locale.forLanguageTag(lang));

        //检查token，标记UncheckToken不检查token
        if (!((HandlerMethod) handler).getMethod().isAnnotationPresent(UncheckToken.class)) {
            String token = "";
            token = Utils.getCookieByName(request.getCookies(), Constants.COOKILE_TOKEN);

            //TODO 校验token步骤
            //if (StringUtils.isEmpty(token) &&  ){
            //    throw new SystemException(ExceptionCode.NEED_LOGIN.getCode(), "token is over time");
            //}
        }
    }

}