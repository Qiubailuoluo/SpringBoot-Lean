# TraceId排障实战路径

## 目标
形成从“接口报错”到“定位根因”的固定动作链。

## 实战步骤
1. 从响应体或响应头拿到 `traceId`。
2. 在日志文件 `bookshop/logs/bookshop-dev.log` 搜索该 `traceId`。
3. 找到同一请求下的 warn/error 记录。
4. 对照错误码回到对应 Service/Mapper 与 SQL。
5. 修复后复测并验证日志恢复正常。

## 排障流程图
阅读提示：先拿 `traceId` 再查日志，按异常层次定位到 DTO、Service 或 Mapper/SQL。
```mermaid
flowchart TD
apiError[ApiErrorFromClient] --> getTraceId[GetTraceId]
getTraceId --> searchLog[SearchLogByTraceId]
searchLog --> findCode[FindErrorCodeAndStack]
findCode --> locateLayer{LocateLayer}
locateLayer -->|Validation| checkDto[CheckDtoValidation]
locateLayer -->|Business| checkService[CheckServiceRule]
locateLayer -->|System| checkMapperSql[CheckMapperAndSql]
checkDto --> retest[Retest]
checkService --> retest
checkMapperSql --> retest
retest --> closeIssue[CloseIssue]
```
## 图解摘要
- 排障入口统一是 `traceId`，先定位日志再判断问题层次。
- 根据异常类型快速分流到 DTO、Service 或 Mapper/SQL 检查。
- 修复后必须回到复测节点确认问题闭环。

## 对应源码入口
- `bookshop/src/main/java/com/bookshop/common/trace/TraceIdFilter.java`
- `bookshop/src/main/java/com/bookshop/exception/GlobalExceptionHandler.java`

## 常见组合
- `VALIDATION_400` + 字段错误：多为 DTO 注解或前端字段名问题。
- `AUTH_TOKEN_INVALID` + 401：多为 token 过期、类型不匹配、缓存失效。
- `SYSTEM_500`：优先看完整堆栈首个业务触发点。

## 下一篇
阅读 `70-日志与观测/04-APM接入预案.md`。
