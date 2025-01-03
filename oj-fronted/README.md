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
