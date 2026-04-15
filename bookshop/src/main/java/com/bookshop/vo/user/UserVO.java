package com.bookshop.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户返回 VO。
 * 作用：定义接口对外返回字段，避免直接暴露 Entity。
 */
@Data
@Schema(description = "用户响应结果")
public class UserVO {

    @Schema(description = "用户ID", example = "1")
    private Long id;
    @Schema(description = "用户名", example = "alice")
    private String username;
    @Schema(description = "显示名称", example = "Alice")
    private String displayName;
    @Schema(description = "状态：1启用，0禁用", example = "1")
    private Integer status;

}
