package com.company.common.exception.handler;

import com.company.common.exception.ServiceException;
import com.company.pojo.vo.Response;
import com.company.common.exception.ExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(Throwable.class)
    public Response handlerException(Throwable throwable) {
        logger.error("全局异常捕获", throwable);

        Response response = new Response();
        response.setMeg("系统异常");
        response.setCode(ExceptionCode.SYSTEM_ERROR.getCode());

        return getError(throwable, response);
    }

    private Response getError(Throwable throwable, Response response){

        if (throwable instanceof ServiceException){
            response.setCode(((ServiceException)throwable).getCode());
            response.setMeg(throwable.getMessage());
        }

        return response;
    }
}
