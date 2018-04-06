package com.company.common.base.handler;

import com.company.common.exception.SystemException;
import com.company.common.tools.Constants;
import com.company.common.exception.ExceptionCode;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

                //interceptorHandler(request, handler);

                return true;
            }
        };
    }

    private void interceptorHandler(HttpServletRequest request, Object handler) {
        //获取请求信息
        //有分平台和版本强跟开启这两个
        String appName = request.getHeader(Constants.APP_NAME);
        String appVersion = request.getHeader(Constants.APP_VERSION);
        //有切换语言需求，这个选项开启
        //String lang = request.getHeader(Constants.LANG);
        if (StringUtils.isEmpty(appName) || StringUtils.isEmpty(appVersion)) {
            throw new SystemException(ExceptionCode.HANDLER_PARAM_ERROR.getCode(), "handler param is null!");
        }

        //设置请求信息
        MDC.put(Constants.APP_NAME, appName);
        MDC.put(Constants.APP_VERSION, appVersion);
        MDC.put("requestId", UUID.randomUUID().toString());
        //语言国际化，有需要开启
        //LocaleContextHolder.setLocale(StringUtils.isEmpty(lang) ? Constants.DEFAULT_LANG : Locale.forLanguageTag(lang));

    }

}