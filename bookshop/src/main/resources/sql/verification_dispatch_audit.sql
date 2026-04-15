-- 验证码发送回执审计表
-- 说明：记录发送结果（成功/失败）与回执元数据，便于后续接入真实厂商后排障与统计。

CREATE TABLE IF NOT EXISTS `verification_dispatch_audit` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `target` VARCHAR(128) NOT NULL COMMENT '验证码目标（邮箱或手机号）',
  `delivery_id` VARCHAR(96) DEFAULT NULL COMMENT '发送回执ID',
  `channel` VARCHAR(32) DEFAULT NULL COMMENT '通道标识（email/sms/mock/stub）',
  `code_masked` VARCHAR(16) DEFAULT NULL COMMENT '脱敏验证码（仅用于审计）',
  `is_mock` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否为 mock/stub 通道',
  `success` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否发送成功',
  `error_message` VARCHAR(255) DEFAULT NULL COMMENT '失败原因',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_target_created_at` (`target`, `created_at`),
  KEY `idx_success_created_at` (`success`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='验证码发送回执审计表';
