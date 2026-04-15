package com.bookshop.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新用户请求 DTO。
 * 作用：约束用户更新接口参数，避免非法输入进入业务层。
 */
@Data
@Schema(description = "用户更新请求参数")
public class UserUpdateDTO {

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "1")
    private Long id;

    @NotBlank(message = "显示名称不能为空")
    @Schema(description = "显示名称", example = "Alice-Updated")
    private String displayName;

    @NotNull(message = "状态不能为空")
    @Schema(description = "用户状态：1启用，0禁用", example = "1")
    private Integer status;

}
