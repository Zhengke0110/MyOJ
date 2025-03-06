# DOJ (Distributed Online Judge) Backend

## 项目介绍

DOJ 是一个分布式在线判题系统的后端服务，基于 Spring Boot 3.x 构建，提供题目管理、在线判题、用户管理等核心功能。

## 技术栈

### 主框架
- Spring Boot 3.4.x - 应用主框架
- Spring MVC - Web 框架
- MyBatis Plus - ORM 框架
- Spring Session - 分布式会话管理
- Spring AOP - 面向切面编程

### 数据存储
- MySQL - 关系型数据库
- Redis - 缓存与会话存储

### 工具类库
- Hutool - Java 工具集
- Apache Commons Lang3 - 通用工具类
- Lombok - 注解简化开发
- Jackson - JSON 处理

## 系统架构

### 核心功能模块
1. 用户模块
   - 用户注册、登录、注销
   - 基于 Spring Session Redis 的分布式登录
   - 用户权限管理 (普通用户/管理员/封禁用户)

2. 帖子模块
   - 帖子的 CRUD 操作
   - 帖子点赞、收藏功能
   - 分页查询与搜索

3. 题目模块
   - 题目管理
   - 在线判题
   - 提交历史

### 系统特性
1. 统一响应处理
   - 封装通用响应类 `BaseResponse`
   - 全局异常处理
   - 自定义错误码

2. 安全特性
   - 自定义权限注解 `@AuthCheck`
   - 基于 AOP 的权限拦截器
   - 全局跨域处理

3. 日志与监控
   - AOP 实现统一日志记录
   - 请求响应日志拦截

4. 性能优化
   - MyBatis Plus 分页插件
   - Long 类型精度丢失处理
   - Redis 缓存支持

## 项目结构
doj-back/
├── annotation - 自定义注解
├── aop - AOP 切面
├── common - 通用类
├── config - 配置类
├── constant - 常量定义
├── controller - 控制器
├── exception - 异常处理
├── mapper - MyBatis 接口
├── model - 数据模型
│ ├── dto - 数据传输对象
│ ├── entity - 实体类
│ ├── enums - 枚举类
│ └── vo - 视图对象
├── service - 业务逻辑
└── utils - 工具类

## 开发环境要求
- JDK 17+
- Maven 3.9+
- MySQL 8.0+
- Redis 6.0+

## 快速开始

1. 克隆项目

```bash:doj-back/README.md
git clone [项目地址]
```

2. 初始化数据库
```bash
# 执行 sql/create_table.sql 脚本创建数据库和表
```

3. 修改配置
```
# 修改 application.yml 中的数据库和 Redis 配置
```

4. 启动项目
```bash
mvn spring-boot:run
```

## 接口文档
- 接口文档使用 Swagger + Knife4j 生成
- 访问地址: http://localhost:8080/doc.html

## 部署说明
1. 打包
```bash
mvn clean package
```

2. 运行
```bash
java -jar doj-backend.jar
```

## 贡献指南
欢迎提交 Issue 和 Pull Request

## 许可证
[Apache License 2.0](LICENSE)
