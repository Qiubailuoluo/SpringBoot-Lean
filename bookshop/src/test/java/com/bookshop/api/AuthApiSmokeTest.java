package com.bookshop.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 认证链路冒烟测试。
 * 作用：验证匿名访问受保护接口被拒绝、错误凭证登录失败。
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthApiSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void protectedApi_withoutToken_shouldReturnLogin498() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("LOGIN_498"));
    }

    @Test
    void login_withInvalidPayload_shouldReturnValidation400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"password\":\"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("VALIDATION_400"))
                .andExpect(jsonPath("$.success").value(false));
    }
}
