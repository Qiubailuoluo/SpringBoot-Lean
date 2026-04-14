package com.bookshop.entity.user;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户实体类（对应数据库表 user_account）。
 * 作用：承载数据库字段，在 service 与 mapper 之间传递。
 */
@Data
public class User {

    private Long id;
    private String username;
    private String passwordHash;
    private String displayName;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
