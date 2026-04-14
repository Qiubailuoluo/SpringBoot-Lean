-- 用户表示例：user_account
-- 说明：执行后即可测试 user 模块 CRUD。

CREATE TABLE IF NOT EXISTS `user_account` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(64) NOT NULL COMMENT '登录名',
  `password_hash` VARCHAR(100) NOT NULL COMMENT '密码哈希（BCrypt）',
  `display_name` VARCHAR(128) NOT NULL COMMENT '显示名称',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';
