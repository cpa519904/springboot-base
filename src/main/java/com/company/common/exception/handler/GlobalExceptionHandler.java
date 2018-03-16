package com.company.common.exception.handler;

import com.company.common.exception.ServiceException;
import com.company.common.base.Response;
import com.company.common.exception.ExceptionCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public Response handlerException(Throwable throwable) {
        Response response = new Response();
        response.setMeg(throwable.getMessage());
        response.setCode(ExceptionCode.SYSTEM_ERROR.getCode());

        return getError(throwable, response);
    }

    private Response getError(Throwable throwable, Response response){

        if (throwable instanceof ServiceException){
            response.setCode(((ServiceException)throwable).getCode());
        }else if (throwable instanceof NoHandlerFoundException) {
            response.setCode(ExceptionCode.NO_HANDLER_ERROR.getCode());
        } else if (throwable instanceof HttpMessageNotReadableException) {
            response.setCode(ExceptionCode.PARAM_TYPE_ERROR.getCode());
        }

        return response;
    }
}
