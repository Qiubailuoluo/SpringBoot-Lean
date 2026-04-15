package com.bookshop.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求 DTO。
 * 作用：承载登录接口入参；骨架阶段仅做校验，真实鉴权在后续 Security 阶段实现。
 */
@Data
@Schema(description = "登录请求参数")
public class LoginRequestDTO {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "alice")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "登录密码", example = "Abc12345")
    private String password;
}
