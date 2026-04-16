package com.bookshop.controller.login;

import com.bookshop.common.response.ApiResponse;
import com.bookshop.dto.login.ChangePasswordRequestDTO;
import com.bookshop.dto.login.LoginRequestDTO;
import com.bookshop.dto.login.RefreshTokenRequestDTO;
import com.bookshop.dto.login.ResetPasswordRequestDTO;
import com.bookshop.dto.login.VerifyCodeSendRequestDTO;
import com.bookshop.service.login.LoginService;
import com.bookshop.service.user.RegistrationGuardService;
import com.bookshop.vo.login.LoginResultVO;
import com.bookshop.vo.login.VerificationSendResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
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
@Tag(name = "Auth", description = "认证与登录接口")
public class LoginController {

    private final LoginService loginService;
    private final RegistrationGuardService registrationGuardService;
    private final boolean exposeVerificationCode;

    public LoginController(
            LoginService loginService,
            RegistrationGuardService registrationGuardService,
            @Value("${security.verification.expose-code:true}") boolean exposeVerificationCode) {
        this.loginService = loginService;
        this.registrationGuardService = registrationGuardService;
        this.exposeVerificationCode = exposeVerificationCode;
    }

    /**
     * 登录：校验用户名与 BCrypt 密码后，返回 access + refresh token。
     */
    @PostMapping("/login")
    @Operation(summary = "登录并获取 access/refresh token")
    public ApiResponse<LoginResultVO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        return ApiResponse.ok("登录成功", loginService.login(requestDTO));
    }

    /**
     * 使用 refresh token 换取新的 access token。
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新 access token")
    public ApiResponse<LoginResultVO> refresh(@Valid @RequestBody RefreshTokenRequestDTO requestDTO) {
        return ApiResponse.ok("刷新成功", loginService.refreshToken(requestDTO.getRefreshToken()));
    }

    /**
     * 发送验证码（当前为 mock/real-stub 可切换）。
     * 真实邮箱/短信厂商 API 暂不接入，此接口返回验证码用于联调。
     */
    @PostMapping({"/verification/send", "/verification/mock-send"})
    @Operation(summary = "发送验证码（mock/real-stub）")
    public ApiResponse<VerificationSendResultVO> sendVerificationCode(@Valid @RequestBody VerifyCodeSendRequestDTO requestDTO) {
        var dispatchResult = registrationGuardService.sendCode(requestDTO.getTarget());
        VerificationSendResultVO vo = new VerificationSendResultVO();
        vo.setTarget(requestDTO.getTarget());
        if (exposeVerificationCode) {
            vo.setCode(dispatchResult.getCode());
        }
        vo.setDeliveryId(dispatchResult.getDeliveryId());
        vo.setChannel(dispatchResult.getChannel());
        vo.setMock(dispatchResult.isMock());
        return ApiResponse.ok("验证码发送成功", vo);
    }

    /**
     * 登出：将当前 access token 加入黑名单，并清理该用户 refresh token。
     */
    @PostMapping("/logout")
    @Operation(summary = "登出并拉黑当前 access token")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        loginService.logout(extractAccessToken(request.getHeader("Authorization")));
        return ApiResponse.ok("登出成功");
    }

    /**
     * 修改密码：校验旧密码后更新新密码，并使旧登录态失效。
     */
    @PostMapping("/password/change")
    @Operation(summary = "修改密码并使旧登录态失效")
    public ApiResponse<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequestDTO requestDTO,
            Authentication authentication,
            HttpServletRequest request) {
        loginService.changePassword(
                authentication.getName(),
                requestDTO.getOldPassword(),
                requestDTO.getNewPassword(),
                extractAccessToken(request.getHeader("Authorization")));
        return ApiResponse.ok("密码修改成功，请重新登录");
    }

    /**
     * 忘记密码重置：校验验证码后更新新密码，并使历史登录态失效。
     */
    @PostMapping("/password/reset")
    @Operation(summary = "重置密码（验证码场景）")
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO requestDTO) {
        loginService.resetPassword(
                requestDTO.getUsername(),
                requestDTO.getVerifyTarget(),
                requestDTO.getVerifyCode(),
                requestDTO.getNewPassword());
        return ApiResponse.ok("密码重置成功，请重新登录");
    }

    private String extractAccessToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.substring(7);
    }
}
