package com.bookshop.vo.user;

/**
 * 用户返回 VO。
 * 作用：定义接口对外返回字段，避免直接暴露 Entity。
 */
public class UserVO {

    private Long id;
    private String username;
    private String displayName;
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
