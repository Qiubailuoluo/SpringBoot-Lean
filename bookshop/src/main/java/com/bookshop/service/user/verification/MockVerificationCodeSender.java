package com.bookshop.service.user.verification;

import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 验证码发送策略（mock）。
 */
@Component
@ConditionalOnProperty(prefix = "security.verification", name = "mode", havingValue = "mock", matchIfMissing = true)
public class MockVerificationCodeSender implements VerificationCodeSender {

    @Override
    public VerificationDispatchResult generateAndSend(String target) {
        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1_000_000));
        String deliveryId = "mock-" + UUID.randomUUID().toString().replace("-", "");
        String channel = target.contains("@") ? "email-mock" : "sms-mock";
        return new VerificationDispatchResult(code, deliveryId, channel, true);
    }
}
