package com.bookshop.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改密码请求 DTO。
 * 作用：承载已登录用户的改密参数（旧密码 + 新密码）。
 */
@Data
@Schema(description = "修改密码请求参数")
public class ChangePasswordRequestDTO {

    @NotBlank(message = "旧密码不能为空")
    @Schema(description = "旧密码", example = "Abc12345")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码（8-20位，含字母和数字）", example = "Abc123456")
    private String newPassword;
}
