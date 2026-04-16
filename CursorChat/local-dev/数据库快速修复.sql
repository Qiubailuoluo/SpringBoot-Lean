-- 本地数据库快速修复脚本（MySQL 8）
-- 目的：在 Flyway 校验失败或历史库未包含 verify_target 字段时，先修到可联调状态。

USE `bookshop`;

-- 0) 补齐验证码审计表（若不存在）
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

-- 1) 补齐 user_account.verify_target 列（若不存在）
SET @verify_col_exists := (
    SELECT COUNT(1)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'user_account'
      AND COLUMN_NAME = 'verify_target'
);

SET @ddl_add_verify_col := IF(
    @verify_col_exists = 0,
    'ALTER TABLE `user_account` ADD COLUMN `verify_target` VARCHAR(128) NULL COMMENT ''验证码目标（邮箱或手机号）'' AFTER `username`',
    'SELECT 1'
);
PREPARE stmt_add_verify_col FROM @ddl_add_verify_col;
EXECUTE stmt_add_verify_col;
DEALLOCATE PREPARE stmt_add_verify_col;

-- 2) 回填 verify_target，避免非空约束失败
SET @ddl_fill_verify_col := 'UPDATE `user_account` SET `verify_target` = CONCAT(`username`, ''@placeholder.local'') WHERE `verify_target` IS NULL OR `verify_target` = ''''';
PREPARE stmt_fill_verify_col FROM @ddl_fill_verify_col;
EXECUTE stmt_fill_verify_col;
DEALLOCATE PREPARE stmt_fill_verify_col;

-- 3) 设为非空
SET @ddl_verify_not_null := 'ALTER TABLE `user_account` MODIFY COLUMN `verify_target` VARCHAR(128) NOT NULL COMMENT ''验证码目标（邮箱或手机号）''';
PREPARE stmt_verify_not_null FROM @ddl_verify_not_null;
EXECUTE stmt_verify_not_null;
DEALLOCATE PREPARE stmt_verify_not_null;

-- 4) 增加唯一索引
SET @uk_verify_exists := (
    SELECT COUNT(1)
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'user_account'
      AND INDEX_NAME = 'uk_verify_target'
);

SET @ddl_add_uk_verify := IF(
    @uk_verify_exists = 0,
    'ALTER TABLE `user_account` ADD UNIQUE KEY `uk_verify_target` (`verify_target`)',
    'SELECT 1'
);
PREPARE stmt_add_uk_verify FROM @ddl_add_uk_verify;
EXECUTE stmt_add_uk_verify;
DEALLOCATE PREPARE stmt_add_uk_verify;

-- 5) 可选：把目标账号绑定到联调邮箱
-- UPDATE `user_account` SET `verify_target` = 'demo@example.com' WHERE `username` = 'qiubai';

-- 6) 校验
SELECT `username`, `verify_target`, `status`
FROM `user_account`
WHERE `username` = 'qiubai';

SELECT COUNT(1) AS audit_table_ready
FROM `verification_dispatch_audit`;
