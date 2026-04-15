# Mapper与SQL执行路径

## 目标
说明请求如何从 Service 进入 MyBatis Mapper 并执行真实 SQL。

## 代码位置
- 图书 Mapper：`mapper/book/BookMapper.java`
- 用户 Mapper：`mapper/user/UserMapper.java`
- SQL 初始化：`resources/sql/book_info.sql`、`resources/sql/user_account.sql`
- MyBatis 配置：`resources/application.yml`

## 执行路径
1. Service 调用 Mapper 接口方法。
2. MyBatis 根据注解 SQL 执行查询或更新。
3. 开启了 `map-underscore-to-camel-case: true`，数据库下划线字段映射到 Java 驼峰属性。

## 关键约束
- 项目采用 MyBatis，不是 Spring Data JPA。
- 当前未使用 Flyway/Liquibase，表结构演进通过 SQL 脚本管理。


# 补充概念

这四个概念都和**数据库打交道**有关，但分属两个不同的维度：

|维度|代表工具|解决的问题|
|---|---|---|
|**ORM（操作数据）**|MyBatis、Spring Data JPA|**怎么把 Java 对象存进数据库 / 怎么把表记录变成 Java 对象**|
|**Migration（管理表结构）**|Flyway、Liquibase|**数据库表结构怎么随着代码版本一起迭代，而不是手动改线上库**|

---

## MyBatis vs JPA —— 操作数据方式

### MyBatis（项目正在用的）

- **思路**：SQL 你写，我帮你执行，然后把结果**映射**到 Java 对象。
    
- **核心文件**：`BookMapper.java`（接口） + 注解里的 `@Select(...)`。
    
- **特点**：**完全掌控 SQL**，复杂查询、多表关联、性能调优非常直接。
    
- **你笔记里的体现**：`map-underscore-to-camel-case: true` 就是把数据库字段 `book_name` 自动转成 Java 属性 `bookName`。
    

### Spring Data JPA（你没用，但常被拿来对比的替代品）

- **思路**：SQL 别写了，我根据**方法名**帮你生成。
    
- **示例**：`interface UserRepository extends JpaRepository<User, Long> { User findByUsername(String name); }`
    
- **特点**：简单 CRUD 极快，不用写一行 SQL。但遇到复杂报表查询时，生成的 SQL 可能不可控，最后还得写原生 SQL。
    

**一句话选择**：

- 追求 SQL 精细控制、数据库老手 → **MyBatis**。
    
- 追求开发速度、简单业务场景多 → **JPA**。
    

---

## Flyway vs Liquibase —— 数据库版本的 Git

你笔记里说 _“当前未使用 Flyway/Liquibase，表结构演进通过 SQL 脚本管理”_，这就是一个**手工操作 vs 自动化**的区别。

### 你现在的做法（手动 SQL 脚本）

1. 开发环境改了表，把 SQL 语句记在 `resources/sql/book_info.sql` 里。
    
2. 要上线了，**人工登录线上数据库**，执行一遍 `ALTER TABLE ...`。
    

- **风险**：万一忘了执行、万一执行错了、万一线上库和本地库结构不一致，服务一跑就报错。
    

### Flyway / Liquibase（自动化版本管理）

它们会在项目启动时**自动检查**数据库的当前版本，然后执行你没执行过的 SQL 脚本。

- **Flyway**：用带版本号的 SQL 文件，比如 `V1__create_user_table.sql`、`V2__add_email_column.sql`。
    
- **Liquibase**：用 XML / YAML / JSON 写变更集，支持回滚。
    

**流程对比**：

```
[手动模式]
你：先停服务 → 登录数据库 → 跑 SQL → 启服务 → 祈祷没写错。


[Flyway 模式]
你：把 SQL 文件放到代码文件夹里。
启动服务：Flyway 自动扫描 → 发现 V3 没执行 → 自动跑 V3 → 服务正常运行。

```
**为什么你笔记强调“未使用”？**  
因为你的项目目前是**小规模演进**或**学习阶段**，手动跑脚本还能接受。一旦进入**团队协作、多环境部署**，这两个工具就变成必选项。

## 下一篇
阅读 [05-统一响应与TraceId流转](./05-统一响应与TraceId流转.md)。
