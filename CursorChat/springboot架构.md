1. 项目定位
面向初创公司 / 中小团队，采用纯 SpringBoot 单体架构，满足业务快速迭代、生产级稳定性、可观测、可运维，未来可平滑演进为微服务。
2. 整体架构分层
plaintext
[接入层]
  Nginx → 负载均衡、HTTPS、静态资源、请求转发

[应用层]
  SpringBoot 单体应用（Docker 容器化）

[数据层]
  MySQL + Redis + Elasticsearch（按需）

[基础设施层]
  配置中心、日志中心、链路追踪、定时任务、对象存储

[运维保障层]
  容器化、CI/CD、监控告警、高可用
3. 核心技术栈
3.1 基础框架
● SpringBoot 
● JDK17 +
● Spring Security（权限认证）
● Exception   全局统一异常处理
● 统一返回封装
3.2 安全认证体系
● Spring Security + JWT
● Redis 存储 Token 黑名单、限流计数
● BCrypt 密码加密
● 接口权限粒度控制
3.3 数据持久层
● MySQL 8.0
● MyBatis / MyBatis-Plus
● Flyway 数据库版本管理
● 慢 SQL 监控（MyBatis 拦截器 + SkyWalking）
3.4 缓存与分布式控制
● Redis（单机 / 主从）
● Redisson 分布式锁（多实例部署防冲突）
● 接口幂等（Redis Token 机制）
3.5 搜索（可选）
● Elasticsearch
3.6 定时任务
● XXL-JOB（支持多实例不重复执行）
● 禁用原生 @Scheduled
3.7 文件存储
● MinIO 或阿里云 OSS
● 禁止本地磁盘存储
3.8 API 文档
● Swagger3 + Knife4j
● 仅 dev / test 环境开放
● 生产环境关闭或权限控制
4. 可观测性体系（生产必备）
4.1 链路追踪
● SkyWalking Agent 自动探针
● MDC 注入 traceId
● 日志格式强制包含 %X{traceId}
● 实现 Log ↔ Trace 联动查询
4.2 日志体系
● Logback 统一日志格式
● 输出 JSON 格式便于收集
● Loki + Grafana 轻量日志检索
● 支持按 traceId 全链路检索
4.3 监控
● SkyWalking 监控接口耗时、JVM、DB 状态
● 基础告警策略
5. 基础设施与配置
5.1 配置中心
● Nacos（仅用作配置管理，不开启注册中心）
● 敏感配置加密
● 多环境隔离：dev/test/prod
● 支持配置热更新
5.2 环境隔离
● dev：开发联调
● test：测试验收
● prod：生产运行
6. 部署与运维体系
6.1 容器化
● Docker + Docker Compose
● 支持一键启动、停止、回滚
6.2 CI/CD
● GitHub Actions / GitLab CI
● 代码提交 → 自动构建镜像 → 自动部署测试环境
6.3 高可用保障
● 多实例部署
● Nginx 负载均衡
● Redis 持久化 / 主从
● MySQL 定期备份
6.4 熔断限流（轻量）
● Resilience4j
● 防止流量打垮数据库、第三方接口超时雪崩
7. 安全规范
● 生产关闭 Swagger
● 数据库密码、Redis 密码不硬编码
● 接口防刷、防重复提交
● 日志不输出敏感信息
8. 适用场景
● 初创公司快速上线
● 团队规模小（1–5 人）
● 业务复杂度中等
● 追求稳定、低成本运维
● 未来可拆分为微服务

9. 当前项目对标状态（后端代码视角）
| 模块 | 状态 | 说明 | 优先级 |
|------|------|------|------|
| Spring Security + JWT + Redis | 已实现 | 已具备登录、刷新、登出、黑名单、改密/重置后旧 token 失效 | P0 |
| BCrypt 密码体系 | 已实现 | 注册/登录/改密/重置均使用 BCrypt | P0 |
| Flyway 数据库版本管理 | 已实现 | 已有 `db/migration` 脚本并纳入启动迁移 | P0 |
| Swagger3/OpenAPI | 已实现 | 使用 `springdoc`，联调入口稳定 | P0 |
| 全局异常与统一返回 | 已实现 | `GlobalExceptionHandler + ApiResponse` | P0 |
| TraceId 基础链路 | 已实现 | `TraceIdFilter + MDC + X-Trace-Id` | P1 |
| 接口权限细粒度（角色级） | 部分实现 | 当前以“是否登录”控制为主，缺角色权限模型 | P1 |
| 慢 SQL 观测 | 未实现 | 暂未接拦截器/APM慢 SQL 追踪 | P1 |
| 配置中心（Nacos） | 未实现 | 当前仍为本地 profile 配置 | P2 |
| SkyWalking APM | 规划中 | 文档已有路线，代码未接入 Agent/OAP | P2 |
| 日志平台（ES/Loki） | 规划中 | 当前以文件日志为主 | P2 |
| XXL-JOB | 未实现 | 当前未引入任务调度中心 | P2 |
| MinIO/OSS | 未实现 | 当前无文件存储业务实现 | P2 |

10. 本阶段建议补充（不含部署）
1) **P0：安全治理收口**
- 增加“用户名与验证码目标绑定”数据字段，封堵重置密码越权窗口。
- 继续完善认证审计（登录成功/失败、改密、重置、刷新、登出）。

2) **P1：代码架构与测试强化**
- 关键接口补集成级鉴权测试（401/403/498/422 断言）。
- 权限模型从“登录可访问”升级到“角色可访问”。

3) **P2：观测能力预留**
- 先完成日志字段规范与告警阈值文档，不急于引入新中间件。