package com.bookshop.vo.login;

import lombok.Data;

/**
 * 验证码发送结果 VO。
 * 作用：统一返回发送回执元数据，便于前端和测试定位发送状态。
 */
@Data
public class VerificationSendResultVO {

    private String target;
    private String code;
    private String deliveryId;
    private String channel;
    private boolean mock;
}
