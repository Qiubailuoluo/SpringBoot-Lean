package com.bookshop.service.login;

import com.bookshop.config.security.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

/**
 * JWT 生成与解析服务。
 */
@Component
public class JwtTokenService {

    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtTokenService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        byte[] decodedKey = Base64.getDecoder().decode(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public String createAccessToken(String username) {
        return createToken(username, TOKEN_TYPE_ACCESS, jwtProperties.getAccessExpireSeconds());
    }

    public String createRefreshToken(String username) {
        return createToken(username, TOKEN_TYPE_REFRESH, jwtProperties.getRefreshExpireSeconds());
    }

    private String createToken(String username, String tokenType, long ttlSeconds) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .id(UUID.randomUUID().toString())
                .claim("type", tokenType)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlSeconds)))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public boolean isTokenType(Claims claims, String expectedType) {
        Object type = claims.get("type");
        return expectedType.equals(type);
    }

    public long getAccessExpireSeconds() {
        return jwtProperties.getAccessExpireSeconds();
    }
}
