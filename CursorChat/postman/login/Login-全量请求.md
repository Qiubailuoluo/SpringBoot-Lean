# Login 模块 Postman 全量请求（JWT + Redis）

## 0. 基础信息

- Base URL: `http://localhost:8080`
- 统一 Header（含 Body 的请求）: `Content-Type: application/json`
- 当前实现为 **JWT + Redis 最小闭环**：
  - `POST /api/auth/login` 返回 `access token` + `refresh token`
  - `POST /api/auth/refresh` 用 refresh token 换新 access token
  - `POST /api/auth/logout` 会将当前 access token 拉黑并清理 refresh token
- `POST /api/auth/password/change` 修改密码后，会清理 refresh token，并使历史 access token 失效（含当前 token）
- `POST /api/auth/password/reset` 忘记密码重置（验证码场景），重置后历史登录态失效
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

- 预期：`success: true`，返回 `data.code`、`data.deliveryId`、`data.channel`、`data.mock`。  
  （真实厂商短信/邮箱 API 暂不接入，`real` 模式当前为占位实现）

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

## 6. 修改密码（登录态）

- Method: `POST`
- URL: `{{baseUrl}}/api/auth/password/change`
- Header:
  - `Authorization: Bearer {{accessToken}}`
  - `Content-Type: application/json`
- Body:

```json
{
  "oldPassword": "Abc12345",
  "newPassword": "Abc123456"
}
```

- 预期：`success: true`，返回“密码修改成功，请重新登录”。
- 注意：改密后历史 access token 会失效，后续调用需重新登录获取新 token。

## 7. 重置密码（忘记密码）

- Method: `POST`
- URL: `{{baseUrl}}/api/auth/password/reset`
- Header:
  - `Content-Type: application/json`
- Body:

```json
{
  "username": "qiubai",
  "verifyTarget": "demo@example.com",
  "verifyCode": "123456",
  "newPassword": "Abc123456"
}
```

- 预期：`success: true`，返回“密码重置成功，请重新登录”。
- 建议流程：先调用 `POST /api/auth/verification/send` 获取验证码，再调用本接口重置。
- 安全约束：`verifyTarget` 必须与该 `username` 注册时绑定目标一致，否则返回 `LOGIN_422`。

## 8. TraceId

- 响应头：`X-Trace-Id` 与失败时响应体中的 `traceId` 规则与 Book/User 模块一致。

## 9. 查询最近验证码回执审计（管理）

- Method: `GET`
- URL: `{{baseUrl}}/api/audit/verification/recent?limit=20`
- Header:
  - `Authorization: Bearer {{accessToken}}`
- Body: 无

- 预期：`success: true`，返回最近回执记录（`deliveryId`、`channel`、`codeMasked`、`success`、`errorMessage`、`createdAt` 等）。

### 9.1 常用筛选参数

- `target`：按邮箱/手机号筛选（完全匹配）
- `success`：按发送结果筛选（`true` / `false`）
- `fromTime`、`toTime`：按时间范围筛选（ISO8601，例如 `2026-04-15T10:00:00`）

示例：

`GET /api/audit/verification/recent?limit=20&target=demo@example.com&success=true&fromTime=2026-04-15T00:00:00&toTime=2026-04-15T23:59:59`

## 10. 关键冒烟回归顺序（建议每次改动后执行）

1. `POST /api/auth/login`（错误密码）  
   - 预期：`code=LOGIN_401`，`success=false`
2. `GET /api/books`（不带 token）  
   - 预期：HTTP `401`，`code=LOGIN_498`
3. `POST /api/auth/login`（正确密码）  
   - 预期：返回 `accessToken + refreshToken`
4. `POST /api/auth/password/change`（登录态）  
   - 预期：成功后旧 token 失效
5. 用旧 token 调 `GET /api/books`  
   - 预期：HTTP `401`，`code=LOGIN_498`
6. `POST /api/auth/password/reset`（验证码场景）  
   - 预期：成功后旧 refresh token 失效，需要重新登录
