# 统一响应与TraceId流转

## 目标
建立“请求可追踪、响应结构稳定”的统一认知。

## 代码位置
- 响应模型：`common/response/ApiResponse.java`
- Trace 过滤器：`common/trace/TraceIdFilter.java`
- 全局异常处理：`exception/GlobalExceptionHandler.java`

## 流转方式
1. `TraceIdFilter` 生成 traceId，写入 MDC 与 request attribute。
2. 正常分支：Controller 返回 `ApiResponse.ok(...)`。
3. 异常分支：异常处理器返回 `ApiResponse.fail(...)`，补充 `timestamp/path/traceId`。
4. 客户端可用 `X-Trace-Id` 或响应体 `traceId` 对齐后端日志。

## 关键收益
- 前端可统一处理成功/失败结构。
- 运维可通过 traceId 串联一次请求的所有日志。

## 下一篇
阅读 [01-异常分层与抛出原则](../20-异常与错误码/01-异常分层与抛出原则.md)。
