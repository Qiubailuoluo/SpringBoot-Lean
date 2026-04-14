# 密码体系与BCrypt实践

## 目标
说明密码在本项目中的存储与校验策略，避免明文与弱加密风险。

## 代码位置
- `java/com/bookshop/config/security/SecurityConfig.java`
- `java/com/bookshop/service/login/impl/LoginServiceImpl.java`
- `resources/sql/user_account_password_migration.sql`

## 实现现状
- `SecurityConfig` 提供 `PasswordEncoder`：`BCryptPasswordEncoder`。
- 登录校验使用 `passwordEncoder.matches(明文, hash)`。
- 数据库字段为 `password_hash`，迁移脚本用于补齐历史数据结构。

## 实践建议
- 禁止在日志记录明文密码。
- 注册/改密只保存 hash，不可逆存储。
- 未来可增加密码复杂度与失效周期策略。

## 下一篇
阅读 [05-登录鉴权时序与失败场景](./05-登录鉴权时序与失败场景.md)。
