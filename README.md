# MyOJ (Distributed Online Judge)

## 项目概述

MyOJ 是一个分布式在线判题系统，提供编程题目的在线评测、用户管理、题目管理等功能。该系统分为前端和后端两个主要组件，采用现代化的技术栈实现。

## 系统架构

本项目采用前后端分离架构：
- **前端**：基于 Vue 3 + TypeScript 构建的 SPA 应用
- **后端**：基于 Spring Boot 3.x 的 RESTful API 服务

## 核心功能

- **用户系统**：注册、登录、个人信息管理
- **题目系统**：题目浏览、详情查看、收藏管理
- **判题系统**：代码提交、在线评测、结果展示
- **社区功能**：帖子发布、点赞、收藏

## 技术栈

### 前端技术栈
- **核心框架**: Vue 3 + TypeScript
- **构建工具**: Vite
- **状态管理**: Pinia + 持久化插件
- **路由**: Vue Router 4
- **UI组件库**: Arco Design Vue, Headless UI, UnoCSS
- **编辑器**: CodeMirror, ByteMD
- **HTTP客户端**: Axios

### 后端技术栈
- **主框架**: Spring Boot 3.4.x, Spring MVC
- **ORM框架**: MyBatis Plus
- **数据存储**: MySQL 8.0+, Redis 6.0+
- **会话管理**: Spring Session
- **工具库**: Hutool, Apache Commons Lang3, Lombok

## 项目结构

```
MyOj/
├── oj-fronted/       # 前端项目
│   └── src/
│       ├── access/      # 权限控制
│       ├── assets/      # 静态资源
│       ├── components/  # 公共组件
│       ├── config/      # 配置文件
│       ├── generated/   # 自动生成的API代码
│       ├── layouts/     # 布局组件
│       ├── router/      # 路由配置
│       ├── store/       # 状态管理
│       └── views/       # 页面组件
│
├── doj-back/        # 后端项目
    ├── annotation/    # 自定义注解
    ├── aop/           # AOP 切面
    ├── common/        # 通用类
    ├── config/        # 配置类
    ├── constant/      # 常量定义
    ├── controller/    # 控制器
    ├── exception/     # 异常处理
    ├── mapper/        # MyBatis 接口
    ├── model/         # 数据模型
    ├── service/       # 业务逻辑
    └── utils/         # 工具类
```

## 快速开始

### 前端开发环境
1. 环境要求：Node.js >= 16, npm >= 7
2. 安装依赖：`cd oj-fronted && npm install`
3. 开发服务：`npm run dev`
4. 构建生产版本：`npm run build`
5. 生成API代码：`npm run generate-api`

### 后端开发环境
1. 环境要求：JDK 17+, Maven 3.9+, MySQL 8.0+, Redis 6.0+
2. 初始化数据库：执行 `doj-back/sql/create_table.sql` 脚本
3. 修改配置：调整 `application.yml` 中的数据库和 Redis 配置
4. 启动项目：`cd doj-back && mvn spring-boot:run`

## 接口文档
- 接口文档使用 Swagger + Knife4j 生成
- 访问地址: http://localhost:8080/doc.html

## 部署指南

### 前端部署
```bash
cd oj-fronted
npm run build
# 将 dist 目录部署到 Web 服务器
```

### 后端部署
```bash
cd doj-back
mvn clean package
java -jar doj-backend.jar
```

## 贡献指南
1. Fork 项目
2. 创建特性分支
3. 提交代码
4. 发起 Pull Request

## 子项目
- [前端项目文档](oj-fronted/README.md)
- [后端项目文档](doj-back/README.md)

## 许可证
本项目采用 [MIT License](LICENSE) 开源协议
