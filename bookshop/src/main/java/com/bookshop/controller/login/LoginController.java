package com.bookshop.controller.login;

import com.bookshop.common.response.ApiResponse;
import com.bookshop.dto.login.LoginRequestDTO;
import com.bookshop.service.login.LoginService;
import com.bookshop.vo.login.LoginResultVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录/认证接口层（骨架）。
 * 作用：暴露 /api/auth 前缀，与后续 JWT 方案路径一致；当前不启用 Spring Security。
 */
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 骨架登录：校验 DTO 非空后返回占位结果，不校验密码、不签发 JWT。
     */
    @PostMapping("/login")
    public ApiResponse<LoginResultVO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        return ApiResponse.ok("骨架：登录接口已接通", loginService.login(requestDTO));
    }

    /**
     * 骨架登出：无状态会话，成功返回即可；后续可扩展 Token 作废逻辑。
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        loginService.logout();
        return ApiResponse.ok("骨架：登出接口已接通（无服务端会话可清理）");
    }
}
