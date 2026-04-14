# HTTP请求到Controller链路总览

## 目标
说明一个请求从 Tomcat 进入系统，到达 Controller 并返回统一响应的全过程。

## 代码位置
- 启动：`java/com/bookshop/BookshopApplication.java`
- Trace 过滤器：`java/com/bookshop/common/trace/TraceIdFilter.java`
- 安全链：`java/com/bookshop/config/security/SecurityConfig.java`
- JWT 过滤器：`java/com/bookshop/config/security/JwtAuthenticationFilter.java`

## JavaWeb 与 SpringBoot 关系（简版）
- JavaWeb 提供基础能力：Servlet 规范、Filter 过滤器、Tomcat 容器。
- Spring MVC 构建在 Servlet 之上：`DispatcherServlet` 负责请求分发到 Controller。
- Spring Boot 负责“自动装配 + 嵌入式 Tomcat + 约定配置”，把 JavaWeb 与 SpringMVC 快速整合成可运行应用。

## 执行链路
1. 嵌入式 Tomcat 接收 HTTP 请求（`spring-boot-starter-web` 默认行为）。
2. `TraceIdFilter` 最先执行，生成 `traceId`，写入 MDC、request attribute、`X-Trace-Id` 响应头。
3. Spring Security FilterChain 执行，按白名单或鉴权策略处理。
4. `JwtAuthenticationFilter` 解析 `Authorization`，校验 access token 与黑名单后写入 `SecurityContext`。
5. `DispatcherServlet` 路由到目标 `@RestController`。
6. Controller 调用 Service，Service 调用 Mapper，Mapper 执行 SQL。
7. 成功响应或异常响应最终都回到 HTTP 客户端。

## 流程图
阅读提示：从左到右看，请求先经过过滤器链，再进入 MVC 与业务层，最后返回统一响应。
```mermaid
flowchart LR
httpClient[HttpClient(客户端)] --> tomcat[EmbeddedTomcat(Tomcat容器)]
tomcat --> traceFilter[TraceIdFilter(链路ID过滤器)]
traceFilter --> securityChain[SecurityFilterChain(安全过滤链)]
securityChain --> jwtFilter[JwtAuthenticationFilter(JWT鉴权过滤器)]
jwtFilter --> dispatcherServlet[DispatcherServlet(MVC分发器)]
dispatcherServlet --> controllerLayer[ControllerLayer(控制层)]
controllerLayer --> serviceLayer[ServiceLayer(业务层)]
serviceLayer --> mapperLayer[MapperLayer(数据访问层)]
mapperLayer --> mysqlDb[MySQL(数据库)]
mysqlDb --> apiResponse[ApiResponse(统一响应)]
apiResponse --> httpClient
```
## 图解摘要
- 请求先被 `TraceIdFilter` 与 Security 过滤链处理，再进入 MVC 分发。
- 业务主干是 Controller -> Service -> Mapper -> MySQL。
- 返回阶段统一落到 `ApiResponse`，便于前后端一致处理。

## 对应源码入口
- `java/com/bookshop/common/trace/TraceIdFilter.java`
- `java/com/bookshop/config/security/JwtAuthenticationFilter.java`

## 关键约束
- 本项目是无状态认证：`SessionCreationPolicy.STATELESS`。
- 未登录/无权限由 Security 异常处理器直接返回 JSON，不进入业务层。

## 下一篇
阅读 [02-Controller入参与校验机制](./02-Controller入参与校验机制.md)。
