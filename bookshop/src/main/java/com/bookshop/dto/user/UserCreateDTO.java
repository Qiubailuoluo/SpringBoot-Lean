package com.bookshop.dto.user;

import jakarta.validation.constraints.NotBlank;
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

}
