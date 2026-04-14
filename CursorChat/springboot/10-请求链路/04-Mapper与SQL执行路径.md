# Mapper与SQL执行路径

## 目标
说明请求如何从 Service 进入 MyBatis Mapper 并执行真实 SQL。

## 代码位置
- 图书 Mapper：`java/com/bookshop/mapper/book/BookMapper.java`
- 用户 Mapper：`java/com/bookshop/mapper/user/UserMapper.java`
- SQL 初始化：`resources/sql/book_info.sql`、`resources/sql/user_account.sql`
- MyBatis 配置：`resources/application.yml`

## 执行路径
1. Service 调用 Mapper 接口方法。
2. MyBatis 根据注解 SQL 执行查询或更新。
3. 开启了 `map-underscore-to-camel-case: true`，数据库下划线字段映射到 Java 驼峰属性。

## 关键约束
- 项目采用 MyBatis，不是 Spring Data JPA。
- 当前未使用 Flyway/Liquibase，表结构演进通过 SQL 脚本管理。

## 下一篇
阅读 [05-统一响应与TraceId流转](./05-统一响应与TraceId流转.md)。
