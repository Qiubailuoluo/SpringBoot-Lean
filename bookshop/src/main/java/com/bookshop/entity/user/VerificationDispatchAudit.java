package com.bookshop.entity.user;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 验证码发送回执审计实体。
 */
@Data
public class VerificationDispatchAudit {

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
