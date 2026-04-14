package com.bookshop.service.user.verification;

import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 验证码发送策略（real-stub）。
 * 作用：模拟未来厂商 API 调用点；当前仍不依赖真实供应商 SDK。
 */
@Component
@ConditionalOnProperty(prefix = "security.verification", name = "mode", havingValue = "real")
public class RealStubVerificationCodeSender implements VerificationCodeSender {

    private static final Logger log = LoggerFactory.getLogger(RealStubVerificationCodeSender.class);

    @Override
    public String generateAndSend(String target) {
        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1_000_000));
        log.info("real-stub 验证码发送：target={}（厂商 API 暂未接入）", target);
        return code;
    }
}
