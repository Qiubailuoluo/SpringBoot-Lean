# Redis令牌缓存与黑名单

## 目标
解释 Redis 在登录态管理中的两个核心用途：refresh 持久化与 access 吊销。

## 代码位置
- `com/bookshop/service/login/TokenCacheService.java`
- `service/login/impl/LoginServiceImpl.java`

## 键设计
- refresh token：`auth:refresh:{username}:{deviceId}`
- access 黑名单：`auth:blacklist:{jwtId}`

## 行为说明
- 登录：按 `username + deviceId` 写入 refresh token，TTL 使用 `refresh-expire-seconds`。
- 刷新：从 token claim 读取 `deviceId`，按设备维度校验 refresh token。
- 登出：将 access 的 `jti` 加入黑名单，并删除当前设备的 refresh token。

## Redis令牌流转图
阅读提示：左侧是登录与登出对缓存的写操作，右侧是刷新令牌读取与匹配校验。
```mermaid
flowchart LR
loginApi["LoginApi(登录接口)"] --> saveRefresh["Set auth_refresh_username_deviceId(写入设备维度refresh缓存)"]
saveRefresh --> accessUse["AccessTokenUse(使用access访问接口)"]
accessUse --> logoutApi["LogoutApi(登出接口)"]
logoutApi --> addBlacklist["Set auth_blacklist_jti(加入access黑名单)"]
logoutApi --> removeRefresh["Delete auth_refresh_username_deviceId(删除设备维度refresh缓存)"]
refreshApi["RefreshApi(刷新接口)"] --> readRefresh["Get auth_refresh_username_deviceId(读取设备维度refresh缓存)"]
readRefresh --> tokenCheck{"TokenMatched(令牌是否匹配)"}
tokenCheck -->|"Yes(是)"| issueNewAccess["IssueNewAccessToken(签发新access)"]
tokenCheck -->|"No(否)"| tokenInvalid["AUTH_TOKEN_INVALID(令牌无效错误码)"]
```
## 图解摘要
- 登录后 refresh token 写入 Redis，作为续签凭证。
- 登出时将 access 的 `jti` 加入黑名单，并清理 refresh 缓存。
- 刷新时必须与缓存一致，不一致直接判定令牌失效。

## 对应源码入口
- `com/bookshop/service/login/TokenCacheService.java`
- `com/bookshop/service/login/impl/LoginServiceImpl.java`

## 关键约束
- 黑名单 TTL 取 access 剩余有效期，避免无意义长驻。
- refresh 只保留当前有效版本，降低重放风险。

## 下一篇
阅读 [04-密码体系与BCrypt实践](./04-密码体系与BCrypt实践.md)。
