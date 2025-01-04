<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout";

import { useUserStore } from "@/store/user";
const userStore = useUserStore();
import { useRoute } from "vue-router";
const route = useRoute();
// TODO 全局初始化函数
setTimeout(() => {
  userStore.setLoginInfo();
}, 1000);
</script>

<template>
  <template v-if="route.path.startsWith('/user')">
    <router-view />
  </template>
  <template v-else>
    <BasicLayout />
  </template>
</template>
