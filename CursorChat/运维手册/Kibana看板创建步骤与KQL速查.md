# Kibana 看板创建步骤与 KQL 速查

> 目标：基于当前已落地字段（`traceId/username/eventType/method/path/status/latencyMs`），快速做出可用的运维看板。  
> 适用 Data View：`bookshop-logs*`

---

## 1. 创建前准备

1. 打开 Kibana `Discover`
2. 选择 Data View：`bookshop-logs*`
3. 时间范围设置为：`过去 15 分钟`（或 `过去 1 小时`）
4. 先确认有访问日志：
   - KQL：`eventType:"HTTP_ACCESS"`

如果没有结果：
- 先检查 SpringBoot 是否有请求流量；
- 检查 Filebeat 是否正在采集；
- 检查时间范围是否过窄。

---

## 2. 看板一：接口性能（10 分钟版）

建议名称：`Bookshop-接口性能-基础版`

### 面板 A：请求量趋势

1. 进入 `Dashboard` -> `Create dashboard`
2. 点 `Create visualization`，选择 `Lens`
3. Data View 选 `bookshop-logs*`
4. X 轴：`@timestamp`
5. Y 轴：`Count`
6. 过滤器：`eventType:"HTTP_ACCESS"`
7. 保存为：`请求量趋势`

### 面板 B：错误请求趋势

1. 新建 Lens
2. X 轴：`@timestamp`
3. Y 轴：`Count`
4. 过滤器：`eventType:"HTTP_ACCESS" and status >= 400`
5. 保存为：`错误请求趋势`

### 面板 C：平均响应耗时

1. 新建 Lens
2. X 轴：`@timestamp`
3. Y 轴：`Average(latencyMs)`
4. 过滤器：`eventType:"HTTP_ACCESS"`
5. 保存为：`平均耗时趋势(ms)`

---

## 3. 看板二：用户操作（10 分钟版）

建议名称：`Bookshop-用户操作-基础版`

### 面板 A：按用户操作次数

1. 新建 Lens
2. 图表类型：条形图
3. 维度：`username.keyword`（Top values）
4. 指标：`Count`
5. 过滤器：`eventType:"HTTP_ACCESS" and username != "anonymous"`
6. 保存为：`用户操作次数`

### 面板 B：用户访问路径 TOP

1. 新建 Lens
2. 维度：`path.keyword`（Top values）
3. 指标：`Count`
4. 过滤器：`eventType:"HTTP_ACCESS" and username:"qiubai"`
5. 保存为：`用户访问路径TOP`

### 面板 C：用户错误操作次数

1. 新建 Lens（Metric）
2. 指标：`Count`
3. 过滤器：`eventType:"HTTP_ACCESS" and username:"qiubai" and status >= 400`
4. 保存为：`用户错误操作次数`

---

## 4. 常用 KQL 速查

### 4.1 基础筛选

- 全部访问日志：  
  `eventType:"HTTP_ACCESS"`

- 指定用户：  
  `eventType:"HTTP_ACCESS" and username:"qiubai"`

- 匿名访问：  
  `eventType:"HTTP_ACCESS" and username:"anonymous"`

### 4.2 错误与异常

- 所有错误状态：  
  `eventType:"HTTP_ACCESS" and status >= 400`

- 登录失败：  
  `eventType:"HTTP_ACCESS" and path:"/api/auth/login" and status >= 400`

### 4.3 性能排查

- 接口慢请求（示例阈值 300ms）：  
  `eventType:"HTTP_ACCESS" and latencyMs >= 300`

- 指定接口耗时：  
  `eventType:"HTTP_ACCESS" and path:"/api/books"`

### 4.4 链路定位

- 按 traceId 追踪：  
  `traceId:"<你的traceId>"`

---

## 5. 验证动作（边测边看）

1. 用 Postman 连续请求 5~10 次接口（成功和失败都发）
2. 回 Kibana 点击 `刷新`
3. 观察：
   - 请求量趋势是否上升
   - 错误趋势是否有波动
   - 用户操作次数是否变化

---

## 6. 下一步建议（P0-2 收口）

- 在日志中继续补充业务事件分类（如 `LOGIN_SUCCESS`、`LOGIN_FAIL`、`PASSWORD_RESET`）
- 新增一个“错误码分布”看板（按 `code` 聚合）
- 后续与 SkyWalking 打通后，用 `traceId` 做日志与链路联动排障
