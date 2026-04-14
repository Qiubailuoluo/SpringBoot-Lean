# Login模块分层解析

## 目标
拆解认证域模块的控制层、业务层、基础能力层协作方式。

## 代码位置
- Controller：`bookshop/src/main/java/com/bookshop/controller/login/LoginController.java`
- Service 接口：`bookshop/src/main/java/com/bookshop/service/login/LoginService.java`
- Service 实现：`bookshop/src/main/java/com/bookshop/service/login/impl/LoginServiceImpl.java`
- JWT 服务：`bookshop/src/main/java/com/bookshop/service/login/JwtTokenService.java`
- Redis 缓存：`bookshop/src/main/java/com/bookshop/service/login/TokenCacheService.java`

## 分层模式
- Controller：接收登录/刷新/登出请求，返回标准响应。
- LoginServiceImpl：认证规则核心，协调 UserMapper、PasswordEncoder、JWT、Redis。
- JwtTokenService/TokenCacheService：下沉为可复用安全基础设施。

## 下一篇
阅读 `40-业务模块拆解/04-common层复用边界.md`。
