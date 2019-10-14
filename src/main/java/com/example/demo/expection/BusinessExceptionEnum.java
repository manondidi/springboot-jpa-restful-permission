package com.example.demo.expection;

public enum BusinessExceptionEnum {
    LOGIN_FAIL(400, "登录失败"),
    USER_UNLOGIN(400, "用户未登录");


    public int code;
    public String message;

    private BusinessExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
