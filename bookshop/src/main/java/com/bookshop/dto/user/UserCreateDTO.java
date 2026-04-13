package com.bookshop.dto.user;

import jakarta.validation.constraints.NotBlank;

/**
 * 新增用户请求 DTO。
 * 作用：限制用户新增接口入参，保证必要字段完整。
 */
public class UserCreateDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "显示名称不能为空")
    private String displayName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
