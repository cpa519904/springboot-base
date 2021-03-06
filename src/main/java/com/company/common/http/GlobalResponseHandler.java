package com.company.common.http;

import com.company.common.annotations.UncheckToken;
import com.company.common.tools.ThreadLocalUtil;
import com.company.pojo.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalResponseHandler extends WebMvcRegistrationsAdapter {

    /*
     *  WebMvcRegistrationsAdapter通过getRequestMappingHandlerAdapter获取RequestMappingHandlerAdapter。
     *  RequestMappingHandlerAdapter重写ReturnValueHandlers方法
     *  传入的视图解析器是jackson
     *  注：ReturnValueHandlers调用视图转换器，所以在此处替换returnValue
     */

    @Autowired
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Override
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        List<HandlerMethodReturnValueHandler> handlerList = new ArrayList<>();
        //采用匿名内部类使代码更简洁，此处也可以用lambda或者继承
        handlerList.add(new RequestResponseBodyMethodProcessor(Collections.singletonList(mappingJackson2HttpMessageConverter)) {
            @Override
            public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
                super.handleReturnValue(packageResult(returnValue, returnType), returnType, mavContainer, webRequest);
            }
        });
        requestMappingHandlerAdapter.setReturnValueHandlers(handlerList);

        //清楚缓存
        ThreadLocalUtil.clean();
        return requestMappingHandlerAdapter;
    }

    private Object packageResult(Object data, MethodParameter returnType) {
        Annotation annotation = returnType.getMethod().getAnnotation(UncheckToken.class);

        if (annotation != null) {
            return data;
        }

        Response response = new Response();
        response.setData(data);
        return response;
    }
}
