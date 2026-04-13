package com.bookshop.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 更新用户请求 DTO。
 * 作用：约束用户更新接口参数，避免非法输入进入业务层。
 */
public class UserUpdateDTO {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    @NotBlank(message = "显示名称不能为空")
    private String displayName;

    @NotNull(message = "状态不能为空")
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
