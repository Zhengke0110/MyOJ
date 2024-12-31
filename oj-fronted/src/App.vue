<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout";

import { useRouter } from "vue-router";
import { useUserStore } from "@/store/user";
import { ACCESSENUM } from "@/access";
const userStore = useUserStore();

// 全局路由拦截器
const router = useRouter();
router.beforeEach((to, from, next) => {
  if (to.meta?.access === ACCESSENUM.ADMIN) {
    if (userStore.getUserRole !== ACCESSENUM.ADMIN) {
      next("/noauth");
      return;
    }
  }
  next();
});

// TODO 全局初始化函数
setTimeout(() => {
  userStore.setLoginInfo("Doj~", ACCESSENUM.ADMIN);
}, 3000);
</script>

<template>
  <BasicLayout />
</template>
