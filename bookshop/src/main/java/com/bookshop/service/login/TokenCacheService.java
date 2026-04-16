package com.bookshop.service.login;

import java.time.Duration;
import java.util.Date;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Token 缓存服务（Redis）。
 * 作用：保存 refresh token 与 access token 黑名单。
 */
@Service
public class TokenCacheService {

    private static final String REFRESH_PREFIX = "auth:refresh:";
    private static final String BLACKLIST_PREFIX = "auth:blacklist:";
    private static final String PASSWORD_CHANGED_AT_PREFIX = "auth:pwd-changed:";

    private final StringRedisTemplate redisTemplate;

    public TokenCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheRefreshToken(String username, String refreshToken, long ttlSeconds) {
        redisTemplate.opsForValue().set(REFRESH_PREFIX + username, refreshToken, Duration.ofSeconds(ttlSeconds));
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(REFRESH_PREFIX + username);
    }

    public void removeRefreshToken(String username) {
        redisTemplate.delete(REFRESH_PREFIX + username);
    }

    public void addAccessBlacklist(String jwtId, long ttlSeconds) {
        if (ttlSeconds <= 0) {
            return;
        }
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + jwtId, "1", Duration.ofSeconds(ttlSeconds));
    }

    public boolean isAccessBlacklisted(String jwtId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jwtId));
    }

    public void markPasswordChangedAt(String username, long epochSeconds) {
        redisTemplate.opsForValue().set(PASSWORD_CHANGED_AT_PREFIX + username, String.valueOf(epochSeconds));
    }

    public boolean isAccessTokenIssuedBeforePasswordChanged(String username, Date issuedAt) {
        if (issuedAt == null) {
            return false;
        }
        String changedAt = redisTemplate.opsForValue().get(PASSWORD_CHANGED_AT_PREFIX + username);
        if (changedAt == null || changedAt.isBlank()) {
            return false;
        }
        try {
            long changedAtEpoch = Long.parseLong(changedAt);
            return issuedAt.toInstant().getEpochSecond() < changedAtEpoch;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
