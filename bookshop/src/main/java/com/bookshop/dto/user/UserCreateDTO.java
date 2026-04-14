package com.bookshop.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 新增用户请求 DTO。
 * 作用：限制用户新增接口入参，保证必要字段完整。
 */
@Data
public class UserCreateDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "显示名称不能为空")
    private String displayName;

    @NotBlank(message = "密码不能为空")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\W_]{8,20}$",
            message = "密码强度不足，需8-20位且包含字母和数字")
    private String password;

    @NotBlank(message = "验证码目标不能为空（邮箱或手机号）")
    private String verifyTarget;

    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
}
