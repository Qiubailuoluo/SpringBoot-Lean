# ES 与 Kibana 本地安装接入教程（Windows）

> 目标：让你能在浏览器 UI 中查看接口性能相关日志、按用户检索操作日志，为后续运维做准备。  
> 说明：本教程先做**本地单机**，优先“可用 + 可看”；后续再做生产化（集群、ILM、权限、备份）。

## 1. 你将得到什么

- `Elasticsearch`：日志存储与检索引擎
- `Kibana`：Web UI（查询、看板、可视化）
- （推荐）`Filebeat`：把 Spring Boot 日志文件采集到 ES

最终你可在浏览器里做：
- 按 `username` 查用户操作日志
- 按 `traceId` 查一次请求全链路日志
- 按 `path`、`status`、`latencyMs` 看接口表现

## 2. 版本建议

- Elasticsearch：`8.x`（与 Kibana 保持同版本）
- Kibana：`8.x`（与 ES 完全一致）
- Filebeat：`8.x`（同样建议一致）

## 3. 安装步骤（本地）

### 3.1 下载

从 Elastic 官方下载以下 zip 包（Windows）：
- Elasticsearch
- Kibana
- Filebeat（可选但强烈建议）

> 建议放到：`D:\tools\elastic\`

### 3.2 启动 Elasticsearch

进入 ES 目录执行：

```powershell
.\bin\elasticsearch.bat
```

默认端口：
- HTTP：`9200`

验证：

```powershell
Invoke-WebRequest http://localhost:9200
```

### 3.3 启动 Kibana

进入 Kibana 目录执行：

```powershell
.\bin\kibana.bat
```

默认端口：
- Kibana UI：`5601`

浏览器访问：
- `http://localhost:5601`

## 4. Spring Boot 日志格式建议（先做最小可用）

为便于 Kibana 检索，建议日志至少包含这些字段：
- `timestamp`
- `level`
- `traceId`
- `username`（有则记录）
- `path`
- `status`
- `latencyMs`
- `message`

你当前项目已具备 `traceId` 基础能力，可在后续日志增强里补齐其余字段。

## 5. Filebeat 采集 Spring Boot 日志（推荐）

### 5.1 创建 Filebeat 配置（示例）

在 `filebeat.yml` 中配置日志输入（按你的日志文件路径调整）：

```yaml
filebeat.inputs:
  - type: filestream
    id: bookshop-log
    enabled: true
    paths:
      - D:/Lean-qiubai/Springboot/Bookshop/bookshop/logs/*.log

output.elasticsearch:
  hosts: ["http://localhost:9200"]
  index: "bookshop-logs-%{+yyyy.MM.dd}"

setup.kibana:
  host: "http://localhost:5601"
```

### 5.2 启动 Filebeat

```powershell
.\filebeat.exe -e -c .\filebeat.yml
```

### 5.3 在 Kibana 创建 Data View

- 打开 Kibana -> Stack Management -> Data Views
- 新建匹配：`bookshop-logs-*`
- 时间字段选 `@timestamp`（若无则按你的时间字段）

## 6. 你最关心的两个看板怎么做

### 6.1 接口性能看板

可按 `path` 聚合并统计：
- 请求总量
- 错误率（`status >= 400`）
- 平均耗时 / P95（基于 `latencyMs`）

### 6.2 用户操作日志看板

过滤字段：
- `username`
- `eventType`（建议后续加：`LOGIN`、`LOGOUT`、`CHANGE_PASSWORD`、`RESET_PASSWORD`）
- `path`
- `traceId`

按用户和时间范围查看操作轨迹，支持导出给运维审计。

## 7. 常见问题排查

- Kibana 打不开：先确认 `5601` 端口、Kibana 控制台有无报错。
- ES 连接失败：确认 `9200` 可访问，版本与 Kibana 一致。
- 看不到日志：检查日志路径、Filebeat 是否在运行、Data View 是否创建正确。
- 中文乱码：统一 UTF-8，日志输出与采集链路都用 UTF-8。

## 8. 生产化前必须补齐（后续）

- 开启认证与权限（ES/Kibana 账号权限）
- 索引生命周期（ILM）与存储配额
- 慢查询与错误日志告警
- 日志脱敏（手机号/邮箱/token）
- 备份与恢复演练

---

如果你愿意，下一步我可以继续给你补一份：
`SkyWalking 本地安装与 Java Agent 接入教程`，这样你可以同时拥有**日志检索 UI（Kibana）+ 链路追踪 UI（SkyWalking）**。
