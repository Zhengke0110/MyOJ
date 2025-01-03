// import axios from "axios";

// const Service = axios.create({
//   baseURL: import.meta.env.VITE_BASE_API,
//   timeout: 5000,
// });

// /**
//  * @description 响应拦截器
//  */
// Service.interceptors.response.use(
//   (response) => {
//     return response.data;
//   },
//   (error) => {
//     // TODO: 提示错误消息
//     return Promise.reject(error);
//   }
// );
// export default Service;

// Add a request interceptor
import axios from "axios";

axios.interceptors.request.use(
  function (config) {
    // console.log("请求", config);
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
