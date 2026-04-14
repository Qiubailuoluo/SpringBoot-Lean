package com.bookshop.vo.login;

import lombok.Data;

/**
 * 登录结果 VO。
 * 作用：对外返回登录凭据（access + refresh）与有效期。
 */
@Data
public class LoginResultVO {

    /** 登录结果说明。 */
    private String message;

    /** 访问令牌（Bearer access token）。 */
    private String token;

    /** 刷新令牌（用于续签 access token）。 */
    private String refreshToken;

    /** 过期时间（秒），表示 access token 过期秒数。 */
    private Long expiresIn;

    /** 令牌类型，固定为 Bearer。 */
    private String tokenType;
}
