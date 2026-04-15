# User模块分层解析

## 目标
理解用户域在注册、状态、唯一性校验方面的实现方式。

## 代码位置
- Controller：`com/bookshop/controller/user/UserController.java`
- Service：`com/bookshop/service/user/UserService.java`
- ServiceImpl：`com/bookshop/service/user/impl/UserServiceImpl.java`
- Mapper：`com/bookshop/mapper/user/UserMapper.java`
- 注册防护：`com/bookshop/service/user/RegistrationGuardService.java`

## 核心关注点
- 用户名唯一性校验。
- 用户状态（启用/禁用）与登录行为联动。
- 注册验证码与频控策略。

## 下一篇
阅读 [03-Login模块分层解析](./03-Login模块分层解析.md)。
