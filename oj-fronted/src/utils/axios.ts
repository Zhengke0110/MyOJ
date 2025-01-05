import axios from "axios";
axios.defaults.withCredentials = true;

axios.interceptors.request.use(
  function (config) {
    // console.log("请求 headers", config.headers);
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
