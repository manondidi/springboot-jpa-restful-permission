package com.example.demo.expection;

public enum BusinessExceptionEnum {
    LOGIN_FAIL(400, "登录失败"),
    USER_UNLOGIN(400, "用户未登录"),
    ROLE_NOT_FOUND(404, "角色不存在"),
    PERMISSION_NOT_FOUND(404, "权限不存在"),
    PERMISSION_PARENT_NOT_FOUND(404, "父权限不存在"),
    PERMISSION_DELETE_HAS_CHIRLDREN(500, "该权限含有子权限,不可直接删除");


    public int code;
    public String message;

    private BusinessExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
