# DOJ 前端项目

- 组件库：[Arco Design](https://arco.design/vue/docs)

## 使用 OPENAPI 生成 Axios 代码

执行命令, 自动生成 axios 的接口

```sh
npm run generate-api
```

![alt text](/images/generate-api.png)

报错时尝试执行如下代码

```sh
npx openapi-typescript-codegen  --input ./OPENAPI/Doj.openapi.json --output ./src/generated --client axios
```

官方文档地址 https://github.com/ferdikoomen/openapi-typescript-codegen

### 如何自定义请求参数

#### 方法一: 直接修改配置文件【推荐】

修改 `generated/core/OpenAPI.ts`

#### 方法二：直接定义 Axios 请求库的全局参数，如 全局请求响应拦截器

```ts
import axios from "axios";

axios.interceptors.request.use(
  function (config) {
    console.log("请求", config);
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);

// Add a response interceptor
axios.interceptors.response.use(
  function (response) {
    return response;
  },
  function (error) {
    return Promise.reject(error);
  }
);
```
