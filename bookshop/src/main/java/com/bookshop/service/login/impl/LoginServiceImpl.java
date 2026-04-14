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
import com.bookshop.vo.login.LoginResultVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.time.Instant;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 登录业务实现。
 * 作用：基于 JWT + Redis 提供登录、刷新与登出。
 */
@Service
public class LoginServiceImpl implements LoginService {

    private final UserMapper userMapper;
    private final JwtProperties jwtProperties;
    private final JwtTokenService jwtTokenService;
    private final TokenCacheService tokenCacheService;
    private final PasswordEncoder passwordEncoder;

    public LoginServiceImpl(
            UserMapper userMapper,
            JwtProperties jwtProperties,
            JwtTokenService jwtTokenService,
            TokenCacheService tokenCacheService,
            PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.jwtProperties = jwtProperties;
        this.jwtTokenService = jwtTokenService;
        this.tokenCacheService = tokenCacheService;
        this.passwordEncoder = passwordEncoder;
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

        String accessToken = jwtTokenService.createAccessToken(user.getUsername());
        String refreshToken = jwtTokenService.createRefreshToken(user.getUsername());
        tokenCacheService.cacheRefreshToken(user.getUsername(), refreshToken, jwtProperties.getRefreshExpireSeconds());
        return buildTokenResult("登录成功", accessToken, refreshToken);
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
        String cachedRefreshToken = tokenCacheService.getRefreshToken(username);
        if (cachedRefreshToken == null || !cachedRefreshToken.equals(refreshToken)) {
            throw new BusinessException(LoginErrorCode.AUTH_TOKEN_INVALID.getCode(), "刷新令牌已失效，请重新登录");
        }

        String accessToken = jwtTokenService.createAccessToken(username);
        return buildTokenResult("刷新成功", accessToken, refreshToken);
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
            tokenCacheService.removeRefreshToken(claims.getSubject());
        } catch (JwtException ignored) {
            // 令牌已失效时登出无需抛错，保持幂等。
        }
    }

    private LoginResultVO buildTokenResult(String message, String accessToken, String refreshToken) {
        LoginResultVO vo = new LoginResultVO();
        vo.setMessage(message);
        vo.setToken(accessToken);
        vo.setRefreshToken(refreshToken);
        vo.setExpiresIn(jwtTokenService.getAccessExpireSeconds());
        vo.setTokenType("Bearer");
        return vo;
    }
}
