# DOJ (Online Judge System) - 前端项目

## 项目简介

DOJ是一个在线判题系统(Online Judge System)的前端项目，使用现代化的Vue 3技术栈开发。该系统允许用户提交代码，系统会自动判断代码的正确性。

## 技术栈

- **核心框架**: Vue 3 + TypeScript
- **构建工具**: Vite
- **状态管理**: Pinia + pinia-plugin-persistedstate
- **路由**: Vue Router 4
- **UI组件库**: 
  - Arco Design Vue
  - Headless UI
  - UnoCSS (原子化CSS)
- **编辑器**:
  - CodeMirror (代码编辑器)
  - ByteMD (Markdown编辑器)
- **HTTP客户端**: Axios
- **工具库**: 
  - VueUse
  - dayjs (日期处理)

## 主要功能

1. **用户系统**
   - 用户注册
   - 用户登录
   - 用户信息管理

2. **题目系统**
   - 题目列表浏览
   - 题目详情查看
   - 代码提交与判题
   - 题目收藏功能

3. **代码编辑器**
   - 支持多种编程语言(Java, C++, Go)
   - 语法高亮
   - 自动补全
   - 代码格式化

## 项目结构

```
oj-fronted/
├── src/
│   ├── access/         # 权限控制
│   ├── assets/         # 静态资源
│   ├── components/     # 公共组件
│   │   ├── AppBackToLogin/  # 返回登录组件
│   │   ├── Avatar/         # 用户头像组件
│   │   ├── BadgeRemove/    # 可移除标签组件
│   │   ├── Badges/        # 标签组件
│   │   └── CodeEditor/    # 代码编辑器组件
│   ├── config/         # 配置文件
│   ├── generated/      # 自动生成的API代码
│   ├── layouts/        # 布局组件
│   ├── router/         # 路由配置
│   ├── store/          # 状态管理
│   └── views/          # 页面组件
```

## 开发指南

### 环境要求

- Node.js >= 16
- npm >= 7

### 安装依赖

```bash
npm install
```

### 开发服务器

```bash
npm run dev
```

### 构建生产版本

```bash
npm run build
```

### 生成API代码

项目使用OpenAPI生成Axios请求代码：

```bash
npm run generate-api
```

如果生成失败，可以尝试：

```bash
npx openapi-typescript-codegen --input ./OPENAPI/Doj.openapi.json --output ./src/generated --client axios
```

## 自定义请求配置

### 方法一：修改配置文件（推荐）

修改 `generated/core/OpenAPI.ts` 文件

### 方法二：配置Axios全局拦截器

```typescript
import axios from "axios";

// 请求拦截器
axios.interceptors.request.use(
  function (config) {
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);

// 响应拦截器
axios.interceptors.response.use(
  function (response) {
    return response;
  },
  function (error) {
    return Promise.reject(error);
  }
);
```

## 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交代码
4. 发起Pull Request

## 许可证

[MIT License](LICENSE)
