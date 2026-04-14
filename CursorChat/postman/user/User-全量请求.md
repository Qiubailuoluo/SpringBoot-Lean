# User 模块 Postman 全量请求

## 0. 基础信息

- Base URL: `http://localhost:8080`
- 统一 Header: `Content-Type: application/json`
- 鉴权说明：
  - `POST /api/users`（注册）允许匿名访问
  - 其余 `/api/users/**` 需 Header：`Authorization: Bearer {{accessToken}}`

## 1. 查询用户列表

- Method: `GET`
- URL: `{{baseUrl}}/api/users`
- Body: 无

## 2. 新增用户

- Method: `POST`
- URL: `{{baseUrl}}/api/users`
- Body:

```json
{
  "username": "qiubai",
  "displayName": "秋白",
  "password": "123456"
}
```

## 3. 按 ID 查询用户

- Method: `GET`
- URL: `{{baseUrl}}/api/users/1`
- Body: 无

## 4. 更新用户

- Method: `PUT`
- URL: `{{baseUrl}}/api/users`
- Body:

```json
{
  "id": 1,
  "displayName": "秋白同学",
  "status": 1
}
```

## 5. 删除用户

- Method: `DELETE`
- URL: `{{baseUrl}}/api/users/1`
- Body: 无

## 6. 异常场景验证（建议）

### 6.1 新增重复用户名

- Method: `POST`
- URL: `{{baseUrl}}/api/users`
- Body:

```json
{
  "username": "qiubai",
  "displayName": "重复用户",
  "password": "123456"
}
```

- 预期：`code = USER_409`

### 6.2 查询不存在用户

- Method: `GET`
- URL: `{{baseUrl}}/api/users/999999`
- 预期：`code = USER_404`

## 7. 异常响应示例（含 traceId）

- 响应头示例：
  - `X-Trace-Id: 5a1ab4d458b54df5a0a547df3eb4f1e2`

```json
{
  "code": "USER_409",
  "success": false,
  "message": "用户名已存在",
  "data": null,
  "timestamp": 1776071727456,
  "path": "/api/users",
  "traceId": "5a1ab4d458b54df5a0a547df3eb4f1e2"
}
```
