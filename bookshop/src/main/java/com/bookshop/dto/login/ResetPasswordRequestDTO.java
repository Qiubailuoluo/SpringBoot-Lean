package com.bookshop.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 重置密码请求 DTO。
 * 作用：承载“忘记密码”场景的参数（用户名、验证码目标、验证码、新密码）。
 */
@Data
@Schema(description = "重置密码请求参数")
public class ResetPasswordRequestDTO {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "alice")
    private String username;

    @NotBlank(message = "验证码目标不能为空（邮箱或手机号）")
    @Schema(description = "验证码目标（邮箱或手机号）", example = "alice@example.com")
    private String verifyTarget;

    @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码", example = "123456")
    private String verifyCode;

    @NotBlank(message = "新密码不能为空")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\W_]{8,20}$",
            message = "新密码强度不足，需8-20位且包含字母和数字")
    @Schema(description = "新密码（8-20位，含字母和数字）", example = "Abc123456")
    private String newPassword;
}
