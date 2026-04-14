# logback配置与环境差异

## 目标
掌握 dev/test/prod 下日志输出差异，便于本地与线上统一排障。

## 代码位置
- `resources/logback-spring.xml`

## 配置差异
- `dev`：输出 `logs/bookshop-dev.log`，`com.bookshop` 日志级别 `DEBUG`。
- `test/prod`：输出 `logs/bookshop.log`，`com.bookshop` 日志级别 `INFO`。
- 默认 profile：仅控制台输出。

## 关键字段
- 日志 pattern 中已包含 `traceId=%X{traceId:-}`，与 `TraceIdFilter` 联动。

## 下一篇
阅读 [02-日志字段与脱敏规范](./02-日志字段与脱敏规范.md)。
