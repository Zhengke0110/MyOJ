import { createApp } from "vue";
import App from "./App.vue";
import { createPinia } from "pinia";
import ArcoVue from "@arco-design/web-vue";
import router from "./router";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";

// CSS
import "virtual:uno.css";
import "normalize.css";
import "./assets/basic.css";
import "@arco-design/web-vue/dist/arco.css";

// Plugins
import "@/utils/axios";
import Particles from "@tsparticles/vue3";
import { loadSlim } from "@tsparticles/slim";

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

createApp(App)
  .use(pinia)
  .use(ArcoVue)
  .use(router)
  .use(Particles, {
    init: async (engine) => {
      await loadSlim(engine);
    },
  })
  .mount("#app");
