package com.bookshop.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 刷新令牌请求 DTO。
 * 作用：承载 refresh token，换取新的 access token。
 */
@Data
public class RefreshTokenRequestDTO {

    @NotBlank(message = "refreshToken 不能为空")
    private String refreshToken;
}
