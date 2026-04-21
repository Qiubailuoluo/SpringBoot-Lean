# Mapper与SQL执行路径

## 目标
说明请求如何从 Service 进入 MyBatis Mapper 并执行真实 SQL。

## 代码位置
- 图书 Mapper：`mapper/book/BookMapper.java`
- 用户 Mapper：`mapper/user/UserMapper.java`
- Flyway 迁移（数据库版本管理工具）：`resources/db/migration/
	- V1__create_book_info.sql`、`
	- V2__create_user_account.sql`、
	- `V3__create_verification_dispatch_audit.sql`
	- `V4__ensure_user_password_hash.sql`、`V5__add_user_verify_target.sql`
- MyBatis 配置：`resources/application.yml`

## 执行路径
1. Service 调用 Mapper 接口方法。
2. MyBatis 根据注解 SQL 执行查询或更新。
3. 开启了 `map-underscore-to-camel-case: true`，数据库下划线字段映射到 Java 驼峰属性。

## 关键约束
- 项目采用 MyBatis，不是 Spring Data JPA。
- 当前已启用 Flyway（`spring.flyway.enabled: true`），表结构演进以 `resources/db/migration` 为主。

## 现状说明（源码对齐）
- 业务查询与更新仍由 MyBatis Mapper 承担，适合 SQL 可控场景。
- 表结构迁移由 Flyway 执行，避免手工 SQL 在多环境漂移。
- `resources/sql` 下脚本可作为学习/初始化参考，但生产迁移以 `db/migration` 为准。

## 下一篇
阅读 [05-统一响应与TraceId流转](./05-统一响应与TraceId流转.md)。
