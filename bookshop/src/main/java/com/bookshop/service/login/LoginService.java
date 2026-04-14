package com.bookshop.service.login;

import com.bookshop.dto.login.LoginRequestDTO;
import com.bookshop.vo.login.LoginResultVO;

/**
 * 登录业务接口。
 * 作用：定义认证相关能力（登录、刷新、登出）。
 */
public interface LoginService {

    /**
     * 登录。
     */
    LoginResultVO login(LoginRequestDTO requestDTO);

    /**
     * 刷新 access token。
     */
    LoginResultVO refreshToken(String refreshToken);

    /**
     * 登出。
     */
    void logout(String accessToken);
}
