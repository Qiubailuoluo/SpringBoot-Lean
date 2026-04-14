# Controller入参与校验机制

## 目标
理解 DTO 校验在 Controller 层的入口职责，以及失败时如何返回统一错误。

## 代码位置
- 图书控制器：`java/com/bookshop/controller/book/BookController.java`
- 登录控制器：`java/com/bookshop/controller/login/LoginController.java`
- 用户控制器：`java/com/bookshop/controller/user/UserController.java`
- 示例 DTO：`java/com/bookshop/dto/book/BookUpdateDTO.java`

## 入口层职责
- 只做参数接收、参数校验、调用服务、封装 `ApiResponse`。
- 不在 Controller 中写 SQL 和复杂业务分支。
- 使用 `@Valid` + Bean Validation 注解约束输入。

## 校验失败路径
1. 参数不满足约束，抛出 `MethodArgumentNotValidException`。
2. `GlobalExceptionHandler` 统一转换为 `VALIDATION_400`。
3. 返回 `ApiResponse.fail(...)`，并携带 `traceId/path/timestamp`。

## 下一篇
阅读 [03-Service编排与事务边界](./03-Service编排与事务边界.md)。
