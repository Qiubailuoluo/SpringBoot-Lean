package com.bookshop.vo.user;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 验证码发送回执审计 VO。
 */
@Data
public class VerificationDispatchAuditVO {

    private Long id;
    private String target;
    private String deliveryId;
    private String channel;
    private String codeMasked;
    private Boolean isMock;
    private Boolean success;
    private String errorMessage;
    private LocalDateTime createdAt;
}
