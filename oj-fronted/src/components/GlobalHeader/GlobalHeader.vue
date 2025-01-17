<template>
  <header class="bg-white">
    <nav
      class="mx-auto flex max-w-7xl items-center justify-between p-6 lg:px-8"
      aria-label="Global"
    >
      <div class="flex lg:flex-1">
        <a href="#" class="-m-1.5 p-1.5">
          <img class="h-8 w-auto" :src="LogoPath" alt="" />
          <span class="text-sm/6 mx-2 font-semibold text-gray-900"
            ><span class="text-4xl">D</span> oj</span
          >
        </a>
      </div>
      <div class="hidden lg:flex lg:gap-x-12">
        <router-link v-for="item in filteredRoutes" :to="item.path">
          <div
            v-if="selectKey === item.path"
            class="mx-auto text-base/6 lg:mx-0 hover:underline bg-gradient-to-r from-custom-gradient-start to-custom-gradient-end text-white font-bold rounded-lg py-1 px-2 focus:outline-none focus:shadow-outline transform transition hover:scale-102 duration-300 ease-in-out animated animated-duration-500 animated-flip-in-y"
          >
            {{ item.name }}
          </div>
          <div class="text-base/6 font-semibold text-gray-900" v-else>
            {{ item.name }}
          </div>
        </router-link>
      </div>
      <div class="hidden lg:flex lg:flex-1 lg:justify-end">
        <router-link class="text-base/6 font-semibold text-gray-900" to="login"
          >Log in <span aria-hidden="true">&rarr;</span></router-link
        >
      </div>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { LogoPath, LayoutMenu } from "@/config";
import { routes } from "@/router/routes";
import { useUserStore } from "@/store/user";
import { ACCESSENUM, CheckACCESS } from "@/access";

// 状态管理 Pinia
const userStore = useUserStore();
const LoginUserInfo = userStore.getLoginInfo;

// filter routes
const filteredRoutes = computed(() => {
  return routes.filter((route) => {
    const NeedACCESS: ACCESSENUM =
      (route.meta?.access as ACCESSENUM) ?? ACCESSENUM.NOLOGIN;
    return (
      !route.redirect &&
      route.meta?.layout === LayoutMenu.BasicLayout &&
      route.meta?.isShow === true &&
      CheckACCESS(LoginUserInfo.userRole, NeedACCESS)
    ); // 排除包含 redirect 属性的路由和通配符路由
  });
});

// 默认主页
const selectKey = ref<string>("/home");

// 路由跳转时，更新选中的菜单项
const router = useRouter();
router.afterEach((to) => (selectKey.value = to.path));
// 解决 页面刷新时，菜单项未选择的小问题
onMounted(() => (selectKey.value = router.currentRoute.value.path));
</script>
