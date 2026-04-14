-- user_account 密码列迁移脚本（从演示账号升级为 BCrypt 持久化密码）
-- 执行前请先备份数据。

ALTER TABLE `user_account`
    ADD COLUMN `password_hash` VARCHAR(100) NULL COMMENT '密码哈希（BCrypt）' AFTER `username`;

-- 为每个历史用户填入 BCrypt 哈希（请在应用或脚本中生成真实哈希后再执行）。
-- 示例：
-- UPDATE user_account SET password_hash = '<BCrypt哈希>' WHERE username = 'qiubai';

ALTER TABLE `user_account`
    MODIFY COLUMN `password_hash` VARCHAR(100) NOT NULL COMMENT '密码哈希（BCrypt）';
