package com.company.common.exception;

public enum ExceptionCode {
    //系统错误为-1
    SYSTEM_ERROR(-1),
    //前端错误200x
    HANDLER_PARAM_ERROR(2000),
    NEED_LOGIN(2001),
    //业务(后端)错误300x
    SERVICE_ERROR(3000),
    USERNAME_OR_PASSWORD_ERROR(3001),
    AUTH_ERROR(3002),
    SIGN_ERROR(3003);

    private int code;

    ExceptionCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
