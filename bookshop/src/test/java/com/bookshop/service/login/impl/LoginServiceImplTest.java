package com.bookshop.service.login.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.bookshop.config.security.JwtProperties;
import com.bookshop.dto.login.LoginRequestDTO;
import com.bookshop.entity.user.User;
import com.bookshop.exception.BusinessException;
import com.bookshop.mapper.user.UserMapper;
import com.bookshop.service.login.JwtTokenService;
import com.bookshop.service.login.TokenCacheService;
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

    private JwtProperties jwtProperties;

    @InjectMocks
    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setRefreshExpireSeconds(604800);
        loginService = new LoginServiceImpl(userMapper, jwtProperties, jwtTokenService, tokenCacheService, passwordEncoder);
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
}
