package com.bookshop.service.user.verification;

/**
 * 验证码发送结果。
 */
public class VerificationDispatchResult {

    private final String code;
    private final String deliveryId;
    private final String channel;
    private final boolean mock;

    public VerificationDispatchResult(String code, String deliveryId, String channel, boolean mock) {
        this.code = code;
        this.deliveryId = deliveryId;
        this.channel = channel;
        this.mock = mock;
    }

    public String getCode() {
        return code;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public String getChannel() {
        return channel;
    }

    public boolean isMock() {
        return mock;
    }
}
