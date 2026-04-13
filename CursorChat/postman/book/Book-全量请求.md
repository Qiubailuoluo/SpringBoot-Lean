# Book 模块 Postman 全量请求

## 0. 基础信息

- Base URL: `http://localhost:8080`
- 统一 Header: `Content-Type: application/json`

## 1. 查询图书列表

- Method: `GET`
- URL: `{{baseUrl}}/api/books`
- Body: 无

## 2. 新增图书

- Method: `POST`
- URL: `{{baseUrl}}/api/books`
- Body:

```json
{
  "name": "Spring Boot 实战",
  "author": "张三",
  "price": 79.90,
  "stock": 100
}
```

## 3. 按 ID 查询图书

- Method: `GET`
- URL: `{{baseUrl}}/api/books/1`
- Body: 无

## 4. 更新图书

- Method: `PUT`
- URL: `{{baseUrl}}/api/books`
- Body:

```json
{
  "id": 1,
  "name": "Spring Boot 实战（第2版）",
  "author": "张三",
  "price": 89.90,
  "stock": 88
}
```

## 5. 删除图书

- Method: `DELETE`
- URL: `{{baseUrl}}/api/books/1`
- Body: 无

## 6. 异常场景验证（建议）

### 6.1 查询不存在图书

- Method: `GET`
- URL: `{{baseUrl}}/api/books/999999`
- 预期：`code = BOOK_404`

### 6.2 删除不存在图书

- Method: `DELETE`
- URL: `{{baseUrl}}/api/books/999999`
- 预期：`code = BOOK_410`

## 7. 异常响应示例（含 traceId）

- 响应头示例：
  - `X-Trace-Id: 2b9ab71047664f9ebf6f4d43b0af3ed1`

```json
{
  "code": "BOOK_404",
  "success": false,
  "message": "图书不存在",
  "data": null,
  "timestamp": 1776071680123,
  "path": "/api/books/999999",
  "traceId": "2b9ab71047664f9ebf6f4d43b0af3ed1"
}
```
