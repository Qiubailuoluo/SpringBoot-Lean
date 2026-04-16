package com.bookshop.service.login.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookshop.config.security.JwtProperties;
import com.bookshop.dto.login.LoginRequestDTO;
import com.bookshop.entity.user.User;
import com.bookshop.exception.BusinessException;
import com.bookshop.mapper.user.UserMapper;
import com.bookshop.service.login.JwtTokenService;
import com.bookshop.service.login.TokenCacheService;
import com.bookshop.service.user.RegistrationGuardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * LoginServiceImpl 单元测试。
 * 作用：校验登录核心分支（成功与鉴权失败）。
 */
@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private TokenCacheService tokenCacheService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RegistrationGuardService registrationGuardService;

    private JwtProperties jwtProperties;

    @InjectMocks
    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setRefreshExpireSeconds(604800);
        loginService =
                new LoginServiceImpl(
                        userMapper,
                        jwtProperties,
                        jwtTokenService,
                        tokenCacheService,
                        passwordEncoder,
                        registrationGuardService);
    }

    @Test
    void login_whenCredentialValid_shouldReturnTokens() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("qiubai");
        dto.setPassword("123456");

        User user = new User();
        user.setUsername("qiubai");
        user.setPasswordHash("hash");
        user.setStatus(1);
        when(userMapper.selectByUsername("qiubai")).thenReturn(user);
        when(passwordEncoder.matches("123456", "hash")).thenReturn(true);
        when(jwtTokenService.createAccessToken("qiubai")).thenReturn("access-token");
        when(jwtTokenService.createRefreshToken("qiubai")).thenReturn("refresh-token");
        when(jwtTokenService.getAccessExpireSeconds()).thenReturn(1800L);

        var result = loginService.login(dto);

        assertEquals("access-token", result.getToken());
        assertEquals("refresh-token", result.getRefreshToken());
        assertEquals(1800L, result.getExpiresIn());
    }

    @Test
    void login_whenPasswordInvalid_shouldThrowBusinessException() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("qiubai");
        dto.setPassword("bad");

        User user = new User();
        user.setUsername("qiubai");
        user.setPasswordHash("hash");
        user.setStatus(1);
        when(userMapper.selectByUsername("qiubai")).thenReturn(user);
        when(passwordEncoder.matches("bad", "hash")).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class, () -> loginService.login(dto));
        assertEquals("LOGIN_401", ex.getCode());
    }

    @Test
    void changePassword_whenOldPasswordWrong_shouldThrowBusinessException() {
        User user = new User();
        user.setUsername("qiubai");
        user.setPasswordHash("old-hash");
        when(userMapper.selectByUsername("qiubai")).thenReturn(user);
        when(passwordEncoder.matches("wrong-old", "old-hash")).thenReturn(false);

        BusinessException ex =
                assertThrows(
                        BusinessException.class,
                        () -> loginService.changePassword("qiubai", "wrong-old", "Abc123456", "access-token"));
        assertEquals("LOGIN_422", ex.getCode());
    }

    @Test
    void changePassword_whenValid_shouldUpdatePasswordAndInvalidateSession() {
        User user = new User();
        user.setUsername("qiubai");
        user.setPasswordHash("old-hash");
        when(userMapper.selectByUsername("qiubai")).thenReturn(user);
        when(passwordEncoder.matches("Abc12345", "old-hash")).thenReturn(true);
        when(passwordEncoder.matches("Abc123456", "old-hash")).thenReturn(false);
        when(passwordEncoder.encode("Abc123456")).thenReturn("new-hash");
        when(jwtTokenService.parseClaims("access-token")).thenThrow(new io.jsonwebtoken.JwtException("mock"));

        loginService.changePassword("qiubai", "Abc12345", "Abc123456", "access-token");

        verify(userMapper).updatePasswordByUsername("qiubai", "new-hash");
        verify(tokenCacheService).removeRefreshToken("qiubai");
        verify(tokenCacheService).markPasswordChangedAt(eq("qiubai"), anyLong());
    }

    @Test
    void resetPassword_whenValid_shouldUpdatePasswordAndInvalidateSessions() {
        User user = new User();
        user.setUsername("qiubai");
        user.setVerifyTarget("demo@example.com");
        user.setPasswordHash("old-hash");
        when(userMapper.selectByUsername("qiubai")).thenReturn(user);
        when(passwordEncoder.matches("Abc123456", "old-hash")).thenReturn(false);
        when(passwordEncoder.encode("Abc123456")).thenReturn("new-hash");

        loginService.resetPassword("qiubai", "demo@example.com", "123456", "Abc123456");

        verify(registrationGuardService).verifyCodeOrThrow("demo@example.com", "123456");
        verify(userMapper).updatePasswordByUsername("qiubai", "new-hash");
        verify(tokenCacheService).removeRefreshToken("qiubai");
        verify(tokenCacheService).markPasswordChangedAt(eq("qiubai"), anyLong());
    }

    @Test
    void resetPassword_whenVerifyTargetMismatch_shouldThrowBusinessException() {
        User user = new User();
        user.setUsername("qiubai");
        user.setVerifyTarget("owner@example.com");
        user.setPasswordHash("old-hash");
        when(userMapper.selectByUsername("qiubai")).thenReturn(user);

        BusinessException ex =
                assertThrows(
                        BusinessException.class,
                        () -> loginService.resetPassword("qiubai", "other@example.com", "123456", "Abc123456"));
        assertEquals("LOGIN_422", ex.getCode());
    }
}
