package com.company.common.exception;

public class SystemException extends RuntimeException{
    private int code;
    private String message;

    public SystemException(String message) {
        super(message);
        this.code = ExceptionCode.SYSTEM_ERROR.getCode();
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
