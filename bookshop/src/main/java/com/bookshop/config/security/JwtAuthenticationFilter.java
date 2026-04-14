package com.bookshop.config.security;

import com.bookshop.service.login.JwtTokenService;
import com.bookshop.service.login.TokenCacheService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT 鉴权过滤器。
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final TokenCacheService tokenCacheService;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService, TokenCacheService tokenCacheService) {
        this.jwtTokenService = jwtTokenService;
        this.tokenCacheService = tokenCacheService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);
        try {
            Claims claims = jwtTokenService.parseClaims(token);
            if (!jwtTokenService.isTokenType(claims, JwtTokenService.TOKEN_TYPE_ACCESS)) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwtId = claims.getId();
            if (tokenCacheService.isAccessBlacklisted(jwtId)) {
                filterChain.doFilter(request, response);
                return;
            }

            String username = claims.getSubject();
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (JwtException ex) {
            // 令牌非法时保持未认证状态，由 AuthenticationEntryPoint 统一返回。
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
