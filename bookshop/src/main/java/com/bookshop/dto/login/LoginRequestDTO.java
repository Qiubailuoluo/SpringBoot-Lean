package com.bookshop.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求 DTO。
 * 作用：承载登录接口入参；骨架阶段仅做校验，真实鉴权在后续 Security 阶段实现。
 */
@Data
public class LoginRequestDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
