package com.bookshop.service.login;

import com.bookshop.dto.login.LoginRequestDTO;
import com.bookshop.vo.login.LoginResultVO;

/**
 * 登录业务接口。
 * 作用：定义认证相关能力；当前为占位实现，不访问数据库、不签发 JWT。
 */
public interface LoginService {

    /**
     * 登录（骨架占位）。
     * 输入：用户名与密码（仅校验 DTO 非空，不做真实校验）。
     * 输出：说明性文案与空的 token 字段。
     */
    LoginResultVO login(LoginRequestDTO requestDTO);

    /**
     * 登出（骨架占位）。
     * 当前无服务端会话与 Token 黑名单，仅预留接口形态。
     */
    void logout();
}
