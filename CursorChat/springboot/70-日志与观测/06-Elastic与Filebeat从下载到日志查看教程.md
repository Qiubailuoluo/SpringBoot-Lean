# Elastic与Filebeat从下载到日志查看教程

## 先回答：我们是 EFK 还是 ELK？
- `EFK` 一般指 `Elasticsearch + Fluentd + Kibana`。
- 你当前项目使用的是 `Elasticsearch + Kibana + Filebeat`（没有 Fluentd）。
- 所以更准确叫法是：**Elastic Stack（含 Beats）**，或者口语说 **ELK + Filebeat**。

## 目标
从零开始在 Windows 本地搭建日志链路，并在 Kibana 中看到 `bookshop` 的日志。

## 1) 需要下载什么
- Elasticsearch（建议与 Kibana 同版本）
- Kibana（与 Elasticsearch 保持同版本）
- Filebeat（同版本，采集本地日志到 Elasticsearch）

建议目录：
- `D:/tools/elastic/elasticsearch-9.1.2`
- `D:/tools/elastic/kibana-9.1.2`
- `D:/tools/elastic/filebeat-9.1.2`

## 2) 启动顺序与验证
阅读提示：严格按顺序启动，避免“服务可用但无日志”的假成功状态。

1. 启动 Elasticsearch
```powershell
cd D:/tools/elastic/elasticsearch-9.1.2
./bin/elasticsearch.bat
```
验证：
```powershell
Invoke-WebRequest http://127.0.0.1:9200
```

2. 启动 Kibana
```powershell
cd D:/tools/elastic/kibana-9.1.2
./bin/kibana.bat
```
验证：
```powershell
Invoke-WebRequest http://127.0.0.1:5601/api/status
```

3. 启动 Spring Boot（产生日志）
```powershell
cd D:/Lean-qiubai/Springboot/Bookshop/bookshop
mvn spring-boot:run
```
验证：
```powershell
Invoke-WebRequest http://127.0.0.1:8080/health
```

4. 启动 Filebeat（采集日志）
```powershell
cd D:/tools/elastic/filebeat-9.1.2
./filebeat.exe test config -c ./filebeat.yml -e
./filebeat.exe test output -c ./filebeat.yml -e
./filebeat.exe -e -c ./filebeat.yml
```

## 3) Filebeat 最小配置示例（按本项目）
你项目日志文件来自 `resources/logback-spring.xml`，默认落盘在 `bookshop/logs/bookshop-dev.log`（dev）。

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

setup.kibana:
  host: "http://127.0.0.1:5601"
```

## 4) 怎么在 Kibana 查看内容
1. 打开 `http://127.0.0.1:5601`
2. 进入 `Stack Management -> Data Views`
3. 创建 Data View：`bookshop-logs*`
4. 时间字段选择 `@timestamp`
5. 进入 Discover，时间范围选“过去 15 分钟”
6. 调用几次接口后刷新，应该看到新日志

## 5) 本项目推荐查询示例
- 按链路查一次请求：
  - `traceId : "xxxx"`
- 按接口路径看错误：
  - `path : "/api/auth/login" and level : "ERROR"`
- 按关键异常查：
  - `message : "AUTH_TOKEN_INVALID"`

## 6) 结构图（你当前项目）
```mermaid
flowchart LR
client["客户端(发起接口请求)"] --> app["SpringBoot应用(写入日志)"]
app --> logFile["bookshop/logs/*.log(日志文件)"]
logFile --> filebeat["Filebeat(采集并发送)"]
filebeat --> es["Elasticsearch(存储与检索)"]
es --> kibana["Kibana(Discover查看)"]
client --> kibana
```

## 7) 常见问题与排障
- Kibana 打不开：
  - 先确认 ES `9200` 正常，再看 Kibana 控制台是否报错。
- Discover 无数据：
  - 检查时间范围、Data View 名、Filebeat 路径是否正确。
- 只看到空索引：
  - 注意是否落在数据流索引（例如 `.ds-bookshop-logs-*`）。
- 认证接口报 500：
  - 先检查 Redis 是否启动（与日志链路不同，但会影响接口可用性）。

## 下一篇
阅读 [03-Trace](./03-Trace.md) 学会用 `traceId` 做日志排障。
