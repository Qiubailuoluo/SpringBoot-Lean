package com.bookshop.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 刷新令牌请求 DTO。
 * 作用：承载 refresh token，换取新的 access token。
 */
@Data
@Schema(description = "刷新令牌请求参数")
public class RefreshTokenRequestDTO {

    @NotBlank(message = "refreshToken 不能为空")
    @Schema(description = "刷新令牌", example = "eyJhbGciOiJIUzI1NiJ9.refresh-token-demo")
    private String refreshToken;
}
