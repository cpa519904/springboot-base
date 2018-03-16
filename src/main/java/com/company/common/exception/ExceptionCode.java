package com.company.common.exception;

public enum ExceptionCode {
    //系统错误为1000
    SYSTEM_ERROR(1000),
    //前端错误200x
    HANDLER_PARAM_ERROR(2000),
    NEED_LOGIN(2001),
    NO_HANDLER_ERROR(2002),
    PARAM_TYPE_ERROR(2003),
    //业务(后端)错误300x
    SERVICE_ERROR(3000),
    NO_FOUND(3001),
    REPEAT_ERROR(3002);

    private int code;

    ExceptionCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
