package com.bookshop.service.user.verification;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 验证码发送策略（mock）。
 */
@Component
@ConditionalOnProperty(prefix = "security.verification", name = "mode", havingValue = "mock", matchIfMissing = true)
public class MockVerificationCodeSender implements VerificationCodeSender {

    @Override
    public String generateAndSend(String target) {
        return String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1_000_000));
    }
}
