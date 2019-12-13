package com.example.demo.expection;

public enum BusinessExceptionEnum {
    LOGIN_FAIL(400, "登录失败"),
    USER_UNLOGIN(400, "用户未登录"),
    CHANGE_PASSWORD_WRONG_OLDPASSWORD(500, "旧密码输入错误"),
    CHANGE_PASSWORD_FAIL(500, "修改密码失败"),
    USER_NOT_FOUND(404, "用户不存在"),
    USER_CAN_NOT_BAN_SELF(500, "不能自我封禁"),
    USER_CAN_NOT_UNBAN_SELF(500, "不能自我解封"),
    ROLE_NOT_FOUND(404, "角色不存在"),
    PERMISSION_NOT_FOUND(404, "权限不存在"),
    PERMISSION_PARENT_NOT_FOUND(404, "父权限不存在"),
    PERMISSION_DELETE_HAS_CHIRLDREN(500, "该权限含有子权限,不可直接删除"),
    COMMON_HAS_NO_PERMISSION(403, "你没有这个操作的权限,禁止访问"),
    UPLOAD_FAIL(500, "上传文件失败"),
    UPLOAD_FAIL_NOT_FILE(500, "没收到任何文件");

    public int code;
    public String message;

    private BusinessExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
