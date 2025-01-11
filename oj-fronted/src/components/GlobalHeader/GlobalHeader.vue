<template>
  <a-row align="center" :wrap="false">
    <a-col flex="auto">
      <a-menu
        mode="horizontal"
        :selected-keys="selectKey"
        @menu-item-click="menuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <div class="flex items-center text-base">
            <img class="h-[48px]" :src="LogoPath" alt="" />
            <div class="ml-[16px] text-slate-600">D OJ</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in filteredRoutes" :key="item.path">{{
          item.name
        }}</a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="100px">
      <div>{{ LoginUserInfo.userName }}</div>
    </a-col>
  </a-row>
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
const selectKey = ref<string[]>(["/home"]);

// 路由跳转时，更新选中的菜单项
const router = useRouter();
const menuClick = (path: string) => router.push(path);
router.afterEach((to) => (selectKey.value = [to.path]));
// 解决 页面刷新时，菜单项未选择的小问题
onMounted(() => (selectKey.value = [router.currentRoute.value.path]));
</script>
