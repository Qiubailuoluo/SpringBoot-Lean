# 03-Filter 链路与接入方式

> 独立成篇，不依赖本仓库其他章。下列为通用写法，各版本类名以官方文档为准。

## 1. Filter 在链上的位置
- 一次请求进入 Tomcat 后、到达目标 **Servlet 之前**（与响应返回时**对称**再经过一次），会依次经过多个 **`javax.servlet.Filter` / `jakarta.servlet.Filter`（依 JDK 与 EE 版本）** 。  
- 执行顺序由**注册顺序**和**各自实现的顺序规则**（如 `Ordered` 或 `@Order`）决定；**顺序错了会导致鉴权、跨域、日志等「看起来不生效」**。

## 2. 典型链长什么样（概念）
- 最外层常是**字符集、Trace、CORS、压缩** 一类。  
- **Spring Security** 在 Filter 里表现为一个 **`DelegatingFilterProxy` → `FilterChainProxy` → 多条 Security 内部 Filter**；仍处在「到达 `DispatcherServlet` 之前」这一大段里。  
- 业务上自己写的 `Filter` 多通过 Spring 管理，这样可注入其他 Bean。

## 3. 在 Spring Boot 里「怎么加」到项目
常见三类（三选一或组合，按团队规范）：

**方式 A：实现 `Filter` 接口 + Spring 组件**  
- 用 `@Component` 声明类，或 `@Bean` 返回 `Filter`；Spring Boot 为 Servlet 3.0+ 会注册到容器。若需**精确控制顺序与 URL 模式**，更常用方式 B。  

**方式 B：`FilterRegistrationBean<YourFilter>`**  
- 在 `@Configuration` 里注册 `FilterRegistrationBean`，可设置 `setOrder(...)`、`addUrlPatterns("/*")` 等，**对顺序和路径最可控**。  

**方式 C：在 Security 的链中插入**（使用 Spring Security 时）  
- 不是另起一个Servlet，而是把自定义逻辑挂到 `SecurityFilterChain` 的某一步，或 `HttpSecurity` 的相应配置。概念上**仍是 Filter**，只是**配置入口是 Security 的 API**。  

## 4. 与 Servlet 的先后（记忆）
- **Filter 环绕 Servlet**；MVC 的 `DispatcherServlet` 是一个 Servlet，因此**业务 Controller 前一定已经走完当前请求路径上该执行的 Filter**。

**上一篇**：[02-JavaWeb-SpringMVC-SpringBoot关系.md](./02-JavaWeb-SpringMVC-SpringBoot关系.md)  
**下一篇**：[00-技术点总览.md](../00-技术点总览.md)
