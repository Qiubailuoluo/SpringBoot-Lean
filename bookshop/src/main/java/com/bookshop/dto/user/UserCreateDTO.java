package com.bookshop.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 新增用户请求 DTO。
 * 作用：限制用户新增接口入参，保证必要字段完整。
 */
@Data
@Schema(description = "用户注册请求参数")
public class UserCreateDTO {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "alice")
    private String username;

    @NotBlank(message = "显示名称不能为空")
    @Schema(description = "显示名称", example = "Alice")
    private String displayName;

    @NotBlank(message = "密码不能为空")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\W_]{8,20}$",
            message = "密码强度不足，需8-20位且包含字母和数字")
    @Schema(description = "密码（8-20位，含字母和数字）", example = "Abc12345")
    private String password;

    @NotBlank(message = "验证码目标不能为空（邮箱或手机号）")
    @Schema(description = "验证码目标（邮箱或手机号）", example = "alice@example.com")
    private String verifyTarget;

    @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码", example = "123456")
    private String verifyCode;
}
