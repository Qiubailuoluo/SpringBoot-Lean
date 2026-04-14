# JWT签发校验与续签机制

## 目标
说明 access/refresh 双令牌在当前实现中的职责分工。

## 代码位置
- `bookshop/src/main/java/com/bookshop/service/login/JwtTokenService.java`
- `bookshop/src/main/java/com/bookshop/service/login/impl/LoginServiceImpl.java`
- `bookshop/src/main/java/com/bookshop/config/security/JwtProperties.java`

## 令牌机制
- 登录成功后签发 access + refresh。
- access 用于访问受保护接口，在过滤器中校验。
- refresh 用于 `/api/auth/refresh` 换取新的 access。
- JWT claim 中包含 `type`，用于识别 token 类型。

## 关键配置
- `security.jwt.secret`
- `security.jwt.access-expire-seconds`
- `security.jwt.refresh-expire-seconds`

## 下一篇
阅读 `30-安全与登录/03-Redis令牌缓存与黑名单.md`。
