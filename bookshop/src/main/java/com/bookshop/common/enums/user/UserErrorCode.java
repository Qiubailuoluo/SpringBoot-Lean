package com.bookshop.common.enums.user;

/**
 * 用户模块错误码枚举。
 * 作用：集中管理 user 业务错误码，便于前后端统一识别错误场景。
 */
public enum UserErrorCode {
    USER_NOT_FOUND("USER_404", "用户不存在"),
    USER_DELETE_FAILED("USER_410", "删除失败，用户不存在"),
    USERNAME_DUPLICATE("USER_409", "用户名已存在");

    private final String code;
    private final String message;

    UserErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
