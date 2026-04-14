package com.bookshop.controller.login;

import com.bookshop.common.response.ApiResponse;
import com.bookshop.dto.login.LoginRequestDTO;
import com.bookshop.dto.login.RefreshTokenRequestDTO;
import com.bookshop.service.login.LoginService;
import com.bookshop.vo.login.LoginResultVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录/认证接口层。
 * 作用：暴露 /api/auth 前缀，提供登录、刷新和登出接口。
 */
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 登录：校验用户名与演示密码后，返回 access + refresh token。
     */
    @PostMapping("/login")
    public ApiResponse<LoginResultVO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        return ApiResponse.ok("登录成功", loginService.login(requestDTO));
    }

    /**
     * 使用 refresh token 换取新的 access token。
     */
    @PostMapping("/refresh")
    public ApiResponse<LoginResultVO> refresh(@Valid @RequestBody RefreshTokenRequestDTO requestDTO) {
        return ApiResponse.ok("刷新成功", loginService.refreshToken(requestDTO.getRefreshToken()));
    }

    /**
     * 登出：将当前 access token 加入黑名单，并清理该用户 refresh token。
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        loginService.logout(extractAccessToken(request.getHeader("Authorization")));
        return ApiResponse.ok("登出成功");
    }

    private String extractAccessToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.substring(7);
    }
}
