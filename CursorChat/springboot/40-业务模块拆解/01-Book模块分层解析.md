# Book模块分层解析

## 目标
用图书模块建立分层设计的参考样例。

## 代码位置
- Controller：`com/bookshop/controller/book/BookController.java`
- Service：`com/bookshop/service/book/BookService.java`
- ServiceImpl：`com/bookshop/service/book/impl/BookServiceImpl.java`
- Mapper：`com/bookshop/mapper/book/BookMapper.java`
- DTO/VO：`com/bookshop/dto/book`、`com/bookshop/vo/book`

## 分层职责
- Controller：接收请求、参数校验、返回 `ApiResponse`。
- Service：封装增删改查业务规则与异常判断。
- Mapper：执行具体 SQL。

## 结构图
阅读提示：从上到下看分层依赖，Controller 仅入参/出参，Service 承接业务，Mapper 落库。
```mermaid
flowchart TB
bookController["BookController(图书控制器)"] --> bookService["BookService(图书服务接口)"]
bookService --> bookServiceImpl["BookServiceImpl(图书服务实现)"]
bookServiceImpl --> bookMapper["BookMapper(图书数据访问)"]
bookMapper --> bookTable["(book_info(图书表))"]
bookController --> requestDto["BookCreateDTOOrBookUpdateDTO(请求DTO)"]
bookServiceImpl --> responseVo["BookVO(返回VO)"]
```
## 图解摘要
- Book 模块严格按 Controller、Service、Mapper 分层，职责边界清晰。
- DTO/VO 与数据库表解耦，避免实体直接暴露给接口层。
- 业务扩展建议放在 Service 层，不破坏 Controller 与 Mapper 简洁性。

## 对应源码入口
- `com/bookshop/controller/book/BookController.java`
- `com/bookshop/service/book/impl/BookServiceImpl.java`

## 常见扩展位
- 上下架状态、库存策略、分页检索、排序字段白名单。

## 下一篇
阅读 [02-User模块分层解析](./02-User模块分层解析.md)。
