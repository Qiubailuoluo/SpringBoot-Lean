# APM接入预案

## 目标
为后续接入 SkyWalking/Zipkin/OpenTelemetry 预留标准化路径。

## 当前基础
- 已有 `traceId` 贯穿过滤器、异常响应、日志输出。
- 可以先用日志检索满足基础链路追踪。

## 接入建议
- 首阶段：保留现有 traceId，不改业务代码，先接 agent 级 APM。
- 二阶段：将 traceId 与 APM trace/span 做字段映射。
- 三阶段：增加慢 SQL、接口耗时、异常率告警看板。

## 风险控制
- 先在 `test` 环境验证性能开销与采样率，再推广到 `prod`。

## 下一篇
阅读 `80-测试与联调/01-测试分层与范围约定.md`。
