package com.company.common.http;

import com.company.common.exception.SystemException;
import com.company.common.tools.Constants;
import com.company.common.exception.ExceptionCode;
import com.company.common.tools.JsonUtil;
import com.company.common.tools.Utils;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.UUID;

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

//                interceptorHandler(request, handler);

                return true;
            }
        };
    }

    private void interceptorHandler(HttpServletRequest request, Object handler) {
        //校验参数
        checkParam(request);
        //语言国际化
        locale(request);
        //验签
        verifySign(request);
        //防重放
        antiReplay();
    }

    private void checkParam(HttpServletRequest request) {
        String appName = request.getHeader(Constants.APP_NAME);
        String appVersion = request.getHeader(Constants.APP_VERSION);
        String sign = request.getHeader(Constants.SIGN);
        if (StringUtils.isEmpty(appName) || StringUtils.isEmpty(appVersion) || StringUtils.isEmpty(sign)) {
            throw new SystemException(ExceptionCode.HANDLER_PARAM_ERROR.getCode(), "handler param is null!");
        }

        //设置请求信息
        MDC.put(Constants.APP_NAME, appName);
        MDC.put(Constants.APP_VERSION, appVersion);
        MDC.put("requestId", UUID.randomUUID().toString());
    }

    private void locale(HttpServletRequest request) {
        String lang = request.getHeader(Constants.LANG);
        LocaleContextHolder.setLocale(StringUtils.isEmpty(lang) ? Locale.SIMPLIFIED_CHINESE : Locale.forLanguageTag(lang));
    }

    private void verifySign(HttpServletRequest request) {
        String paramData = JsonUtil.Object2String(request.getParameterMap());
        String sign = request.getHeader(Constants.SIGN);
        if (Utils.checkSign(paramData, sign, "")) {}
    }

    private void antiReplay() {
        //timestamp + uuid
    }
}