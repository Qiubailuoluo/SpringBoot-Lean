SET @col_exists := (
    SELECT COUNT(1)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'user_account'
      AND COLUMN_NAME = 'password_hash'
);

SET @ddl := IF(
    @col_exists = 0,
    'ALTER TABLE `user_account` ADD COLUMN `password_hash` VARCHAR(100) NULL COMMENT ''密码哈希（BCrypt）'' AFTER `username`',
    'SELECT 1'
);

PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
