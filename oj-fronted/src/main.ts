import { createApp } from "vue";
import App from "./App.vue";
import { createPinia } from "pinia";
import ArcoVue from "@arco-design/web-vue";
import router from "./router";

// CSS
import "virtual:uno.css";
import "normalize.css";
import "@arco-design/web-vue/dist/arco.css";

// Plugins
import "@/utils/axios";
// import "@/access";

const pinia = createPinia();

createApp(App).use(pinia).use(ArcoVue).use(router).mount("#app");
