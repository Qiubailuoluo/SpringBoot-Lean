package com.bookshop.common.enums.login;

/**
 * 登录/认证模块错误码枚举。
 * 作用：集中管理认证相关错误码；当前为骨架阶段，部分枚举预留给 JWT + Spring Security 阶段使用。
 */
public enum LoginErrorCode {
    /** 对外错误码 {@code LOGIN_501}；显式拒绝或能力未就绪时使用（骨架阶段多为预留）。 */
    AUTH_NOT_IMPLEMENTED("LOGIN_501", "认证能力尚未实现，请先完成 Spring Security + JWT 接入"),
    /** 对外错误码 {@code LOGIN_401}；JWT 阶段登录失败或凭证无效时使用。 */
    AUTH_INVALID_CREDENTIALS("LOGIN_401", "用户名或密码错误"),
    /** 对外错误码 {@code LOGIN_403}；用于被禁用账户或无权限访问。 */
    AUTH_FORBIDDEN("LOGIN_403", "当前账号无权限访问"),
    /** 对外错误码 {@code LOGIN_498}；Token 无效、过期或被吊销。 */
    AUTH_TOKEN_INVALID("LOGIN_498", "登录态已失效，请重新登录");

    private final String code;
    private final String message;

    LoginErrorCode(String code, String message) {
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
