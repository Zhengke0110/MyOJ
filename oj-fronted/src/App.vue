<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout";
import { LayoutMenu } from "@/config";
import { useUserStore } from "@/store/user";
const userStore = useUserStore();
import { useRoute } from "vue-router";
import { onMounted } from "vue";
const route = useRoute();

// TODO 全局初始化函数
onMounted(() => userStore.setLoginInfo());
</script>

<template>
  <template v-if="route.meta?.layout === LayoutMenu.BasicLayout">
    <BasicLayout />
  </template>
  <template v-else>
    <RouterView />
  </template>
</template>
