# Postman用例组织方式

## 目标
统一接口集合结构，提升回归效率。

## 目录建议
- `Auth`：登录、刷新、登出、验证码。
- `Book`：增删改查、分页查询。
- `User`：注册、查询、状态相关接口。
- `System`：`/health` 与基础探活接口。

## 环境变量建议
- `baseUrl`
- `accessToken`
- `refreshToken`
- `traceId`（可选，便于调试记录）

## 断言建议
- 统一断言 `success/code/message` 字段。
- 对关键接口增加状态码与业务码双重断言。

## 下一篇
阅读 [03-接口回归验收清单](./03-接口回归验收清单.md)。
