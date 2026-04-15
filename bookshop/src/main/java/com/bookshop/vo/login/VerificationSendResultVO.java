package com.bookshop.vo.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 验证码发送结果 VO。
 * 作用：统一返回发送回执元数据，便于前端和测试定位发送状态。
 */
@Data
@Schema(description = "验证码发送结果")
public class VerificationSendResultVO {

    @Schema(description = "验证码目标（邮箱或手机号）", example = "alice@example.com")
    private String target;
    @Schema(description = "验证码（mock/stub联调用）", example = "123456")
    private String code;
    @Schema(description = "发送回执ID", example = "mock-8fcb55b881c94e22b2ebf5d49e5f1234")
    private String deliveryId;
    @Schema(description = "发送通道", example = "email-mock")
    private String channel;
    @Schema(description = "是否为 mock 通道", example = "true")
    private boolean mock;
}
