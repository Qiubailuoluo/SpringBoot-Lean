package com.bookshop.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新用户请求 DTO。
 * 作用：约束用户更新接口参数，避免非法输入进入业务层。
 */
@Data
public class UserUpdateDTO {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    @NotBlank(message = "显示名称不能为空")
    private String displayName;

    @NotNull(message = "状态不能为空")
    private Integer status;

}
