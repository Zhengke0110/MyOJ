<template>
  <a-row style="margin-bottom: 16px" align="center">
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
            <div class="ml-[16px] text-slate-600">🐶 OJ</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in filteredRoutes" :key="item.path">{{
          item.name
        }}</a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="100px">
      <div>{{ useStore.getUserName }}</div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import { useRouter } from "vue-router";
import { LogoPath } from "@/config";
import { routes } from "@/router/routes";
import { useUserStore } from "@/store/user";

// filter routes
const filteredRoutes = computed(() => {
  return routes.filter(
    (route) => !route.redirect && route.path !== "/:pathMatch(.*)*" // 排除包含 redirect 属性的路由和通配符路由
  );
});

// 默认主页
const selectKey = ref<string[]>(["/"]);

// 路由跳转时，更新选中的菜单项
const router = useRouter();
const menuClick = (path: string) => {
  console.log(path);
  router.push(path);
};
router.afterEach((to) => (selectKey.value = [to.path]));

// 状态管理 Pinia
const useStore = useUserStore();
</script>
