package com.bookshop.vo.user;

import lombok.Data;

/**
 * 用户返回 VO。
 * 作用：定义接口对外返回字段，避免直接暴露 Entity。
 */
@Data
public class UserVO {

    private Long id;
    private String username;
    private String displayName;
    private Integer status;

}
