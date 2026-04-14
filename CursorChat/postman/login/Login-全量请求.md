# Login 模块 Postman 全量请求（JWT + Redis）

## 0. 基础信息

- Base URL: `http://localhost:8080`
- 统一 Header（含 Body 的请求）: `Content-Type: application/json`
- 当前实现为 **JWT + Redis 最小闭环**：
  - `POST /api/auth/login` 返回 `access token` + `refresh token`
  - `POST /api/auth/refresh` 用 refresh token 换新 access token
  - `POST /api/auth/logout` 会将当前 access token 拉黑并清理 refresh token
  - `POST /api/auth/verification/send` 发送验证码（当前按 `security.verification.mode` 走 `mock/real-stub`，返回验证码便于测试）
  - Redis 默认连接 `127.0.0.1:6379`（当前无密码）
  - 用户密码使用 `user_account.password_hash`（BCrypt）校验
  - `POST /api/users` 允许匿名创建用户（用于注册/初始化账号），其余 `/api/users/**` 默认需 Bearer token

## 1. 模拟发送验证码（开发联调）

- Method: `POST`
- URL: `{{baseUrl}}/api/auth/verification/send`
- Body:

```json
{
  "target": "demo@example.com"
}
```

- 预期：`success: true`，`data.code` 返回 6 位验证码（真实厂商短信/邮箱 API 暂不接入，`real` 模式当前为占位实现）。

## 2. 登录

- Method: `POST`
- URL: `{{baseUrl}}/api/auth/login`
- Body:

```json
{
  "username": "qiubai",
  "password": "123456"
}
```

- 预期：`success: true`，返回 `data.token`、`data.refreshToken`、`data.expiresIn`、`data.tokenType=Bearer`。

## 3. 刷新 Token

- Method: `POST`
- URL: `{{baseUrl}}/api/auth/refresh`
- Body:

```json
{
  "refreshToken": "从登录响应中复制"
}
```

- 预期：`success: true`，返回新的 `data.token`。

## 4. 登出

- Method: `POST`
- URL: `{{baseUrl}}/api/auth/logout`
- Header:
  - `Authorization: Bearer {{accessToken}}`
- Body: 无

- 预期：`success: true`，再次携带同一 access token 访问受保护接口将被拒绝。

## 5. 鉴权失败示例（建议）

- 登录密码错误：`code = LOGIN_401`
- refresh token 失效或伪造：`code = LOGIN_498`
- 受保护接口未带 Bearer token：`code = LOGIN_498`

## 6. TraceId

- 响应头：`X-Trace-Id` 与失败时响应体中的 `traceId` 规则与 Book/User 模块一致。
