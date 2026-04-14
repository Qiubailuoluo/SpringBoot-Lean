package com.bookshop.vo.login;

import lombok.Data;

/**
 * 登录结果 VO。
 * 作用：对外返回登录结果占位结构；token 等字段在 JWT 接入后填充。
 */
@Data
public class LoginResultVO {

    /** 骨架阶段说明文案，便于联调时理解当前进度。 */
    private String message;

    /** 访问令牌，骨架阶段固定为 null。 */
    private String token;

    /** 过期时间（秒），骨架阶段固定为 null。 */
    private Long expiresIn;
}
