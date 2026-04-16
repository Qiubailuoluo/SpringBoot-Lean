package com.bookshop.service.login.impl;

import com.bookshop.common.enums.login.LoginErrorCode;
import com.bookshop.config.security.JwtProperties;
import com.bookshop.dto.login.LoginRequestDTO;
import com.bookshop.entity.user.User;
import com.bookshop.exception.BusinessException;
import com.bookshop.mapper.user.UserMapper;
import com.bookshop.service.login.JwtTokenService;
import com.bookshop.service.login.LoginService;
import com.bookshop.service.login.TokenCacheService;
import com.bookshop.service.user.RegistrationGuardService;
import com.bookshop.vo.login.LoginResultVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.time.Instant;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 登录业务实现。
 * 作用：基于 JWT + Redis 提供登录、刷新与登出。
 */
@Service
public class LoginServiceImpl implements LoginService {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\W_]{8,20}$");

    private final UserMapper userMapper;
    private final JwtProperties jwtProperties;
    private final JwtTokenService jwtTokenService;
    private final TokenCacheService tokenCacheService;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationGuardService registrationGuardService;

    public LoginServiceImpl(
            UserMapper userMapper,
            JwtProperties jwtProperties,
            JwtTokenService jwtTokenService,
            TokenCacheService tokenCacheService,
            PasswordEncoder passwordEncoder,
            RegistrationGuardService registrationGuardService) {
        this.userMapper = userMapper;
        this.jwtProperties = jwtProperties;
        this.jwtTokenService = jwtTokenService;
        this.tokenCacheService = tokenCacheService;
        this.passwordEncoder = passwordEncoder;
        this.registrationGuardService = registrationGuardService;
    }

    @Override
    public LoginResultVO login(LoginRequestDTO requestDTO) {
        User user = userMapper.selectByUsername(requestDTO.getUsername());
        if (user == null) {
            throw new BusinessException(
                    LoginErrorCode.AUTH_INVALID_CREDENTIALS.getCode(),
                    LoginErrorCode.AUTH_INVALID_CREDENTIALS.getMessage());
        }
        if (user.getPasswordHash() == null || !passwordEncoder.matches(requestDTO.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(
                    LoginErrorCode.AUTH_INVALID_CREDENTIALS.getCode(),
                    LoginErrorCode.AUTH_INVALID_CREDENTIALS.getMessage());
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(LoginErrorCode.AUTH_FORBIDDEN.getCode(), "账号已禁用，请联系管理员");
        }

        String deviceId = normalizeDeviceId(requestDTO.getDeviceId());
        String accessToken = jwtTokenService.createAccessToken(user.getUsername(), deviceId);
        String refreshToken = jwtTokenService.createRefreshToken(user.getUsername(), deviceId);
        tokenCacheService.cacheRefreshToken(user.getUsername(), deviceId, refreshToken, jwtProperties.getRefreshExpireSeconds());
        return buildTokenResult("登录成功", accessToken, refreshToken, deviceId);
    }

    @Override
    public LoginResultVO refreshToken(String refreshToken) {
        Claims claims;
        try {
            claims = jwtTokenService.parseClaims(refreshToken);
        } catch (JwtException ex) {
            throw new BusinessException(LoginErrorCode.AUTH_TOKEN_INVALID.getCode(), LoginErrorCode.AUTH_TOKEN_INVALID.getMessage());
        }
        if (!jwtTokenService.isTokenType(claims, JwtTokenService.TOKEN_TYPE_REFRESH)) {
            throw new BusinessException(LoginErrorCode.AUTH_TOKEN_INVALID.getCode(), LoginErrorCode.AUTH_TOKEN_INVALID.getMessage());
        }

        String username = claims.getSubject();
        String deviceId = normalizeDeviceId(claims.get(JwtTokenService.CLAIM_DEVICE_ID, String.class));
        String cachedRefreshToken = tokenCacheService.getRefreshToken(username, deviceId);
        if (cachedRefreshToken == null || !cachedRefreshToken.equals(refreshToken)) {
            throw new BusinessException(LoginErrorCode.AUTH_TOKEN_INVALID.getCode(), "刷新令牌已失效，请重新登录");
        }

        String accessToken = jwtTokenService.createAccessToken(username, deviceId);
        return buildTokenResult("刷新成功", accessToken, refreshToken, deviceId);
    }

    @Override
    public void logout(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            return;
        }
        try {
            Claims claims = jwtTokenService.parseClaims(accessToken);
            if (!jwtTokenService.isTokenType(claims, JwtTokenService.TOKEN_TYPE_ACCESS)) {
                return;
            }
            long ttlSeconds = Math.max(0, claims.getExpiration().toInstant().getEpochSecond() - Instant.now().getEpochSecond());
            tokenCacheService.addAccessBlacklist(claims.getId(), ttlSeconds);
            String deviceId = normalizeDeviceId(claims.get(JwtTokenService.CLAIM_DEVICE_ID, String.class));
            tokenCacheService.removeRefreshToken(claims.getSubject(), deviceId);
        } catch (JwtException ignored) {
            // 令牌已失效时登出无需抛错，保持幂等。
        }
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword, String currentAccessToken) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new BusinessException(LoginErrorCode.AUTH_TOKEN_INVALID.getCode(), LoginErrorCode.AUTH_TOKEN_INVALID.getMessage());
        }
        if (user.getPasswordHash() == null || !passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new BusinessException(LoginErrorCode.AUTH_PASSWORD_INVALID.getCode(), "旧密码不正确");
        }
        if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
            throw new BusinessException(LoginErrorCode.AUTH_PASSWORD_INVALID.getCode(), "新密码强度不足，需8-20位且包含字母和数字");
        }
        if (passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new BusinessException(LoginErrorCode.AUTH_PASSWORD_INVALID.getCode(), "新密码不能与旧密码相同");
        }

        userMapper.updatePasswordByUsername(username, passwordEncoder.encode(newPassword));
        tokenCacheService.removeAllRefreshTokens(username);
        tokenCacheService.markPasswordChangedAt(username, Instant.now().getEpochSecond());
        addCurrentAccessTokenToBlacklist(currentAccessToken);
    }

    @Override
    public void resetPassword(String username, String verifyTarget, String verifyCode, String newPassword) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new BusinessException(LoginErrorCode.AUTH_INVALID_CREDENTIALS.getCode(), LoginErrorCode.AUTH_INVALID_CREDENTIALS.getMessage());
        }
        if (user.getVerifyTarget() == null || !user.getVerifyTarget().equals(verifyTarget)) {
            throw new BusinessException(LoginErrorCode.AUTH_PASSWORD_INVALID.getCode(), "验证码目标与账号不匹配");
        }
        registrationGuardService.verifyCodeOrThrow(verifyTarget, verifyCode);
        if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
            throw new BusinessException(LoginErrorCode.AUTH_PASSWORD_INVALID.getCode(), "新密码强度不足，需8-20位且包含字母和数字");
        }
        if (user.getPasswordHash() != null && passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new BusinessException(LoginErrorCode.AUTH_PASSWORD_INVALID.getCode(), "新密码不能与旧密码相同");
        }

        userMapper.updatePasswordByUsername(username, passwordEncoder.encode(newPassword));
        tokenCacheService.removeAllRefreshTokens(username);
        tokenCacheService.markPasswordChangedAt(username, Instant.now().getEpochSecond());
    }

    private void addCurrentAccessTokenToBlacklist(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            return;
        }
        try {
            Claims claims = jwtTokenService.parseClaims(accessToken);
            if (!jwtTokenService.isTokenType(claims, JwtTokenService.TOKEN_TYPE_ACCESS)) {
                return;
            }
            long ttlSeconds = Math.max(0, claims.getExpiration().toInstant().getEpochSecond() - Instant.now().getEpochSecond());
            tokenCacheService.addAccessBlacklist(claims.getId(), ttlSeconds);
        } catch (JwtException ignored) {
            // 当前 token 非法时无需额外处理，改密结果仍应成功返回。
        }
    }

    private LoginResultVO buildTokenResult(String message, String accessToken, String refreshToken, String deviceId) {
        LoginResultVO vo = new LoginResultVO();
        vo.setMessage(message);
        vo.setToken(accessToken);
        vo.setRefreshToken(refreshToken);
        vo.setExpiresIn(jwtTokenService.getAccessExpireSeconds());
        vo.setTokenType("Bearer");
        vo.setDeviceId(deviceId);
        return vo;
    }

    private String normalizeDeviceId(String deviceId) {
        if (deviceId == null || deviceId.isBlank()) {
            return JwtTokenService.DEFAULT_DEVICE_ID;
        }
        return deviceId.trim();
    }
}
