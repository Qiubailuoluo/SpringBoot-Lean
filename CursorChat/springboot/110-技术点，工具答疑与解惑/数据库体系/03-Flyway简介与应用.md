# 03-Flyway 简介与应用

> 独立成篇；以 Spring Boot 内嵌 Flyway 的常见用法为叙述对象。

## 1. 解决什么问题
- 数据库的**表结构、索引、数据补丁**会随版本迭代，需要**可重复、可审计、可按环境应用**的变更方式。  
- **Flyway** 用**版本化迁移脚本**（常放在 `classpath` 的固定目录里）在应用启动或约定时机**顺序执行**未执行过的脚本，并记录**已应用版本**（`flyway_schema_history` 等元表），避免同一脚本对同一环境执行两次。

## 2. 和 MyBatis 的分工
- **Flyway**：**改库结构/种子数据**（`CREATE` / `ALTER`、必要时 `INSERT` 初始化数据）。  
- **MyBatis**：在**已存在的表**上做业务读写；通常**不**在业务代码里随意执行 DDL。

## 3. 在 Spring Boot 里怎么「实际应用」
- 引入 **Flyway 依赖**（如 `flyway-core`，MySQL 场景常配 `flyway-mysql`）。  
- 在 `application` 配置里**开启** Flyway，并指定**脚本位置**（常见为 `classpath:db/migration`）。  
- 在资源目录中按**命名规范**放脚本，例如 `V1__init.sql`、`V2__add_column.sql`（前缀与引擎版本规则以官方为准）。  
- 启动时若配置正确、库可达，则 Flyway 先跑完待执行迁移，**再**由业务与 MyBatis 使用当前库表结构。  

**常用配置键（仅作认路，以你项目 yml 为准）**  
- `spring.flyway.enabled`  
- `spring.flyway.locations`  
- 空库/已有库场景可能用到 `baseline-on-migrate` 等策略项。

## 4. 实践中要注意的
- **多环境**（dev/test/prod）应对接**不同库**或**严格策略**，避免误在错误环境执行。  
- 大表变更、锁表、回滚策略属于**发布与 DBA 范畴**，不在此展开，但应知道 Flyway 只管「**执行哪段脚本、执行到哪一版**」。

**上一篇**：[02-MySQL与项目集成简说.md](./02-MySQL与项目集成简说.md)  
**下一篇**：[04-Maven依赖与表实体Mapper配对.md](./04-Maven依赖与表实体Mapper配对.md)
