# 01-ELK 与 Filebeat 协作链路

## 1. 组成与作用
- `Elasticsearch`：日志存储与检索引擎，支持按字段查询与聚合。
- `Kibana`：基于 ES 的查询、可视化、仪表盘。
- `Filebeat`：轻量采集器，读取应用日志文件并投递到日志平台。

## 2. 协作链路
```mermaid
flowchart LR
  APP[Spring Boot 应用日志文件] --> FB[Filebeat 采集]
  FB --> ES[Elasticsearch 索引]
  ES --> KB[Kibana 查询与图表]
```

## 3. 在项目里的落点（概念）
- 应用侧负责把日志按统一格式输出（建议包含 `traceId`、接口路径、错误码等）。
- Filebeat 负责把文件日志可靠推送到 ES。
- 运维与排障在 Kibana 中按 `traceId` 或错误码回溯调用链。

**上一篇**：[00-本章导读.md](./00-本章导读.md)  
**下一篇**：[02-Swagger与Postman联调实践.md](./02-Swagger与Postman联调实践.md)
