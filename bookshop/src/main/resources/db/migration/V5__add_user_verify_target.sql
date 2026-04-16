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

SET @ddl_fill_verify_col := 'UPDATE `user_account` SET `verify_target` = CONCAT(`username`, ''@placeholder.local'') WHERE `verify_target` IS NULL OR `verify_target` = ''''';
PREPARE stmt_fill_verify_col FROM @ddl_fill_verify_col;
EXECUTE stmt_fill_verify_col;
DEALLOCATE PREPARE stmt_fill_verify_col;

SET @ddl_verify_not_null := 'ALTER TABLE `user_account` MODIFY COLUMN `verify_target` VARCHAR(128) NOT NULL COMMENT ''验证码目标（邮箱或手机号）''';
PREPARE stmt_verify_not_null FROM @ddl_verify_not_null;
EXECUTE stmt_verify_not_null;
DEALLOCATE PREPARE stmt_verify_not_null;

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
