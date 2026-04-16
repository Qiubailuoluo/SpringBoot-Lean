# Elastic 9.1.2 本地部署实操手册（Windows）

> 导航：建议先阅读 `CursorChat/运维手册/本地可观测启动顺序.md`，再按本文执行详细配置。

> 适用对象：你已下载以下 3 个 zip 包  
> - `elasticsearch-9.1.2-windows-x86_64.zip`  
> - `kibana-9.1.2-windows-x86_64.zip`  
> - `filebeat-oss-9.1.2-windows-x86_64.zip`  
>
> 目标：在浏览器中打开 Kibana，并让 SpringBoot 日志进入 Elasticsearch 可检索。

---

## 1. 目录规划（先做）

建议统一放到一个目录，避免后续路径混乱：

```text
D:\tools\elastic\
  ├─ elasticsearch-9.1.2\
  ├─ kibana-9.1.2\
  └─ filebeat-oss-9.1.2\
```

操作：
1. 新建目录 `D:\tools\elastic\`
2. 3 个 zip 全部解压到该目录
3. 确认每个目录下有 `bin` 子目录

---

## 2. Elasticsearch 配置与启动

### 2.1 修改配置文件

编辑：
- `D:\tools\elastic\elasticsearch-9.1.2\config\elasticsearch.yml`

追加（或确认）以下配置：

```yaml
cluster.name: bookshop-local
node.name: node-1

network.host: 127.0.0.1
http.port: 9200

discovery.type: single-node

# 本地开发先关闭安全认证，后续再开启
xpack.security.enabled: false
```

### 2.2 启动 ES

打开 PowerShell：

```powershell
cd D:\tools\elastic\elasticsearch-9.1.2
.\bin\elasticsearch.bat
```

看到类似日志表示启动成功（关键字）：
- `started`
- `publish_address`
- `127.0.0.1:9200`

### 2.3 验证 ES

新开一个 PowerShell：

```powershell
Invoke-WebRequest http://127.0.0.1:9200
```

返回 JSON 即成功。

---

## 3. Kibana 配置与启动

### 3.1 修改配置文件

编辑：
- `D:\tools\elastic\kibana-9.1.2\config\kibana.yml`

追加：

```yaml
server.host: "127.0.0.1"
server.port: 5601
elasticsearch.hosts: ["http://127.0.0.1:9200"]
i18n.locale: "zh-CN"
```

### 3.2 启动 Kibana

新开 PowerShell：

```powershell
cd D:\tools\elastic\kibana-9.1.2
.\bin\kibana.bat
```

### 3.3 浏览器验证

打开：
- `http://127.0.0.1:5601`

能进入 Kibana 页面即成功。

---

## 4. Filebeat 配置与启动（采集 SpringBoot 日志）

> 你项目日志路径按实际调整。  
> 如果当前项目只打控制台日志，先让项目输出到文件（`logs/*.log`），再采集。

### 4.1 修改 filebeat.yml

编辑：
- `D:\tools\elastic\filebeat-oss-9.1.2\filebeat.yml`

可用最小配置示例（直接替换核心段落）：

```yaml
filebeat.inputs:
  - type: filestream
    id: bookshop-log
    enabled: true
    paths:
      - D:/Lean-qiubai/Springboot/Bookshop/bookshop/logs/*.log

output.elasticsearch:
  hosts: ["http://127.0.0.1:9200"]
  index: "bookshop-logs-%{+yyyy.MM.dd}"

setup.template.enabled: true
setup.template.name: "bookshop-logs"
setup.template.pattern: "bookshop-logs-*"
```

### 4.2 启动 Filebeat

PowerShell 执行：

```powershell
cd D:\tools\elastic\filebeat-oss-9.1.2
.\filebeat.exe -e -c .\filebeat.yml
```

看到持续采集日志输出即正常。

---

## 5. Kibana 中查看日志（最关键）

1. 打开 `http://127.0.0.1:5601`
2. 进入 `Stack Management` -> `Data Views`
3. 创建 Data View：
   - Name: `bookshop-logs-*`
   - Index pattern: `bookshop-logs-*`
   - Time field: `@timestamp`（若存在）
4. 进入 `Discover`，选择该 Data View
5. 查看是否有日志流入

---

## 6. 给你项目的检索建议（运维常用）

后续日志字段建议统一包含：
- `traceId`
- `username`
- `path`
- `status`
- `latencyMs`
- `eventType`

这样就能在 Kibana 做：
- 按 `traceId` 查一次请求
- 按 `username` 查用户操作轨迹
- 按 `path` 看接口错误与耗时

---

## 7. 启动顺序（务必按这个）

1. `Elasticsearch`
2. `Kibana`
3. `SpringBoot 项目`
4. `Filebeat`

---

## 8. 常见问题排查

### 8.1 Kibana 启动失败
- 先确认 ES 可访问：`http://127.0.0.1:9200`
- 确认 `kibana.yml` 的 `elasticsearch.hosts` 地址正确

### 8.2 Filebeat 启动但没日志
- 检查日志路径是否存在
- 检查项目是否真正写入 `logs/*.log`
- 用绝对路径，Windows 路径建议使用 `/`

### 8.3 端口冲突
- ES 默认 `9200`，Kibana 默认 `5601`
- 若冲突，修改 yml 后重启对应组件

### 8.4 中文乱码
- 保证日志文件编码 UTF-8
- PowerShell 与编辑器尽量统一 UTF-8

---

## 9. 下一步建议（你当前阶段）

1. 先跑通“日志能进 Kibana Discover”
2. 再补结构化字段（`traceId/username/path/status/latencyMs/eventType`）
3. 最后做可视化看板（接口性能、用户操作审计）

---

如果你愿意，下一步我可以继续给你补一份：
`SkyWalking-9.x 本地安装与 Java Agent 接入手册`，让你把“链路追踪 UI”也一起跑起来。
