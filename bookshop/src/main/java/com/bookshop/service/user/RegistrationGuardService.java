package com.bookshop.service.user;

import com.bookshop.common.enums.user.UserErrorCode;
import com.bookshop.exception.BusinessException;
import com.bookshop.service.user.verification.VerificationCodeSender;
import java.time.Duration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 注册安全守卫服务。
 * 作用：验证码发送/校验与注册限流。
 */
@Service
public class RegistrationGuardService {

    private static final String VERIFY_CODE_PREFIX = "auth:verify:code:";
    private static final String VERIFY_SEND_LIMIT_PREFIX = "auth:verify:send-limit:";
    private static final String REGISTER_IP_LIMIT_PREFIX = "auth:register:ip:";

    private final StringRedisTemplate redisTemplate;
    private final VerificationCodeSender verificationCodeSender;

    public RegistrationGuardService(StringRedisTemplate redisTemplate, VerificationCodeSender verificationCodeSender) {
        this.redisTemplate = redisTemplate;
        this.verificationCodeSender = verificationCodeSender;
    }

    public String sendCode(String target) {
        String sendLimitKey = VERIFY_SEND_LIMIT_PREFIX + target;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(sendLimitKey))) {
            throw new BusinessException(
                    UserErrorCode.REGISTER_TOO_FREQUENT.getCode(),
                    "验证码发送过于频繁，请60秒后重试");
        }

        String code = verificationCodeSender.generateAndSend(target);
        redisTemplate.opsForValue().set(VERIFY_CODE_PREFIX + target, code, Duration.ofMinutes(5));
        redisTemplate.opsForValue().set(sendLimitKey, "1", Duration.ofSeconds(60));
        return code;
    }

    public void verifyCodeOrThrow(String target, String code) {
        String cachedCode = redisTemplate.opsForValue().get(VERIFY_CODE_PREFIX + target);
        if (cachedCode == null || !cachedCode.equals(code)) {
            throw new BusinessException(
                    UserErrorCode.VERIFICATION_CODE_INVALID.getCode(),
                    UserErrorCode.VERIFICATION_CODE_INVALID.getMessage());
        }
        redisTemplate.delete(VERIFY_CODE_PREFIX + target);
    }

    public void checkRegisterRateLimit(String clientIp) {
        String limitKey = REGISTER_IP_LIMIT_PREFIX + clientIp;
        Long count = redisTemplate.opsForValue().increment(limitKey);
        if (count != null && count == 1) {
            redisTemplate.expire(limitKey, Duration.ofHours(1));
        }
        if (count != null && count > 20) {
            throw new BusinessException(
                    UserErrorCode.REGISTER_TOO_FREQUENT.getCode(),
                    "当前IP注册过于频繁，请1小时后再试");
        }
    }
}
