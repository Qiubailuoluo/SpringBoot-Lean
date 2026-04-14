package com.bookshop.service.login.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.bookshop.dto.login.LoginRequestDTO;
import org.junit.jupiter.api.Test;

/**
 * LoginServiceImpl 单元测试。
 * 作用：锁定骨架阶段占位行为，避免后续接入 JWT 时误删契约。
 */
class LoginServiceImplTest {

    private final LoginServiceImpl loginService = new LoginServiceImpl();

    @Test
    void login_returnsPlaceholderWithoutToken() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("demo");
        dto.setPassword("secret");

        var result = loginService.login(dto);

        assertNull(result.getToken());
        assertNull(result.getExpiresIn());
        assertEquals(true, result.getMessage().contains("骨架阶段"));
        assertEquals(true, result.getMessage().contains("demo"));
    }
}
