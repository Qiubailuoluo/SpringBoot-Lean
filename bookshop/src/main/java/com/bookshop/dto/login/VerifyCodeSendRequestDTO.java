package com.bookshop.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 验证码发送请求 DTO（模拟）。
 */
@Data
@Schema(description = "验证码发送请求参数")
public class VerifyCodeSendRequestDTO {

    @NotBlank(message = "验证码目标不能为空（邮箱或手机号）")
    @Schema(description = "验证码目标（邮箱或手机号）", example = "alice@example.com")
    private String target;
}
