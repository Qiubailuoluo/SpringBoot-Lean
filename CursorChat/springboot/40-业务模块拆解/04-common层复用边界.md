# common层复用边界

## 目标
明确 `common` 目录应沉淀什么，避免“万能工具层”失控。

## 当前 common 能力
- 统一响应：`com/bookshop/common/response`
- 链路追踪：`com/bookshop/common/trace`
- 错误码枚举：`com/bookshop/common/enums`

## 建议边界
- 放入 `common`：跨模块稳定复用、与业务语义弱耦合的能力。
- 不放入 `common`：只被单模块使用、强业务语义的逻辑。
- 新增公共能力需给出至少两个模块复用证明。

## 下一篇
阅读 [05-新增模块标准模板](./05-新增模块标准模板.md)。
