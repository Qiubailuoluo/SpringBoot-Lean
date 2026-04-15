package com.bookshop.vo.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录结果 VO。
 * 作用：对外返回登录凭据（access + refresh）与有效期。
 */
@Data
@Schema(description = "登录结果")
public class LoginResultVO {

    /** 登录结果说明。 */
    @Schema(description = "结果消息", example = "登录成功")
    private String message;

    /** 访问令牌（Bearer access token）。 */
    @Schema(description = "访问令牌", example = "eyJhbGciOiJIUzI1NiJ9.access-token-demo")
    private String token;

    /** 刷新令牌（用于续签 access token）。 */
    @Schema(description = "刷新令牌", example = "eyJhbGciOiJIUzI1NiJ9.refresh-token-demo")
    private String refreshToken;

    /** 过期时间（秒），表示 access token 过期秒数。 */
    @Schema(description = "accessToken 过期秒数", example = "1800")
    private Long expiresIn;

    /** 令牌类型，固定为 Bearer。 */
    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType;
}
