package com.bookshop.service.user.verification;

/**
 * 验证码发送策略接口。
 * 作用：隔离 mock 与真实厂商发送实现，便于后续扩展。
 */
public interface VerificationCodeSender {

    /**
     * 生成并发送验证码。
     *
     * @param target 验证码目标（邮箱或手机号）
     * @return 实际发送的验证码（mock/real-stub 阶段用于联调）
     */
    VerificationDispatchResult generateAndSend(String target);
}
