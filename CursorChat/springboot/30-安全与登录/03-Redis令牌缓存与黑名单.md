# Redis令牌缓存与黑名单

## 目标
解释 Redis 在登录态管理中的两个核心用途：refresh 持久化与 access 吊销。

## 代码位置
- `bookshop/src/main/java/com/bookshop/service/login/TokenCacheService.java`
- `bookshop/src/main/java/com/bookshop/service/login/impl/LoginServiceImpl.java`

## 键设计
- refresh token：`auth:refresh:{username}`
- access 黑名单：`auth:blacklist:{jwtId}`

## 行为说明
- 登录：写入 refresh token，TTL 使用 `refresh-expire-seconds`。
- 刷新：读取并校验 refresh token 是否匹配。
- 登出：将 access 的 `jti` 加入黑名单，并删除 refresh token。

## Redis令牌流转图
阅读提示：左侧是登录与登出对缓存的写操作，右侧是刷新令牌读取与匹配校验。
```mermaid
flowchart LR
loginApi[LoginApi] --> saveRefresh[Set auth_refresh_username]
saveRefresh --> accessUse[AccessTokenUse]
accessUse --> logoutApi[LogoutApi]
logoutApi --> addBlacklist[Set auth_blacklist_jti]
logoutApi --> removeRefresh[Delete auth_refresh_username]
refreshApi[RefreshApi] --> readRefresh[Get auth_refresh_username]
readRefresh --> tokenCheck{TokenMatched}
tokenCheck -->|Yes| issueNewAccess[IssueNewAccessToken]
tokenCheck -->|No| tokenInvalid[AUTH_TOKEN_INVALID]
```
## 图解摘要
- 登录后 refresh token 写入 Redis，作为续签凭证。
- 登出时将 access 的 `jti` 加入黑名单，并清理 refresh 缓存。
- 刷新时必须与缓存一致，不一致直接判定令牌失效。

## 对应源码入口
- `bookshop/src/main/java/com/bookshop/service/login/TokenCacheService.java`
- `bookshop/src/main/java/com/bookshop/service/login/impl/LoginServiceImpl.java`

## 关键约束
- 黑名单 TTL 取 access 剩余有效期，避免无意义长驻。
- refresh 只保留当前有效版本，降低重放风险。

## 下一篇
阅读 `30-安全与登录/04-密码体系与BCrypt实践.md`。
