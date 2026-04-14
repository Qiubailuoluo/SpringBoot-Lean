package com.bookshop.config.security;

import com.bookshop.common.enums.login.LoginErrorCode;
import com.bookshop.common.response.ApiResponse;
import com.bookshop.common.trace.TraceIdFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 未认证访问处理器。
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ApiResponse<Void> body = ApiResponse.fail(
                LoginErrorCode.AUTH_TOKEN_INVALID.getCode(),
                LoginErrorCode.AUTH_TOKEN_INVALID.getMessage(),
                Instant.now().toEpochMilli(),
                request.getRequestURI(),
                String.valueOf(request.getAttribute(TraceIdFilter.TRACE_ID_KEY)));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
