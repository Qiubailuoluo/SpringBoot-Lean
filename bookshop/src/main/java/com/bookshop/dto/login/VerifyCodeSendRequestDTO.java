package com.bookshop.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 验证码发送请求 DTO（模拟）。
 */
@Data
public class VerifyCodeSendRequestDTO {

    @NotBlank(message = "验证码目标不能为空（邮箱或手机号）")
    private String target;
}
