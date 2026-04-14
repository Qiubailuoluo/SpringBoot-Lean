# Login 模块 Postman 全量请求（骨架）

## 0. 基础信息

- Base URL: `http://localhost:8080`
- 统一 Header（含 Body 的请求）: `Content-Type: application/json`
- 当前为**占位实现**：不查库、不签发 JWT；成功响应 `code` 为 `SUCCESS`，`data` 内 `token` / `expiresIn` 为 `null`。

## 1. 登录（占位）

- Method: `POST`
- URL: `{{baseUrl}}/api/auth/login`
- Body:

```json
{
  "username": "demo",
  "password": "any"
}
```

- 预期：`success: true`，`data.message` 含占位说明，`data.token` 与 `data.expiresIn` 为 `null`。

## 2. 登出（占位）

- Method: `POST`
- URL: `{{baseUrl}}/api/auth/logout`
- Body: 无

- 预期：`success: true`，提示骨架登出已接通。

## 3. 校验失败示例（可选）

- 请求体省略 `username` 或 `password`，或传空字符串。
- 预期：`code = VALIDATION_400`（以全局校验为准）。

## 4. TraceId

- 响应头：`X-Trace-Id` 与失败时响应体中的 `traceId` 规则与 Book/User 模块一致。
