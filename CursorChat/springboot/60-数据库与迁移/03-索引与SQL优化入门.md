# 索引与SQL优化入门

## 目标
给出当前项目最实用的 SQL 优化检查点。

## 代码位置
- `bookshop/src/main/java/com/bookshop/mapper/book/BookMapper.java`
- `bookshop/src/main/java/com/bookshop/mapper/user/UserMapper.java`

## 基础检查项
- 高频等值查询字段是否有索引（如用户名）。
- 分页查询是否有稳定排序字段。
- `where` 条件是否命中联合索引最左前缀。
- 避免在 SQL 中对索引列做函数计算。

## 观测建议
- 开发环境可结合 MyBatis SQL 输出检查慢查询趋势。
- 对关键 SQL 使用 `EXPLAIN` 验证执行计划。

## 下一篇
阅读 `60-数据库与迁移/04-测试数据与回滚策略.md`。
