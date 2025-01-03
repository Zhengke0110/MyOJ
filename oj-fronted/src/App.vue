<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout";

import { useUserStore } from "@/store/user";
const userStore = useUserStore();

// TODO 全局初始化函数
setTimeout(() => {
  userStore.setLoginInfo();
}, 1000);
</script>

<template>
  <BasicLayout />
</template>
