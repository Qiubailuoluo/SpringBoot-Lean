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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 已认证但权限不足时的处理器。
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        ApiResponse<Void> body = ApiResponse.fail(
                LoginErrorCode.AUTH_FORBIDDEN.getCode(),
                LoginErrorCode.AUTH_FORBIDDEN.getMessage(),
                Instant.now().toEpochMilli(),
                request.getRequestURI(),
                String.valueOf(request.getAttribute(TraceIdFilter.TRACE_ID_KEY)));
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
