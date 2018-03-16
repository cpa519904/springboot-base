package com.company.common.exception;

public class ServiceException extends GlobalException{

    public ServiceException(int code, String message) {
        super(code, message);
    }
}
