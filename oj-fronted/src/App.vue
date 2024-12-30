<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout";

import { useRouter } from "vue-router";
import { useUserStore } from "@/store/user";
const userStore = useUserStore();

const router = useRouter();
router.beforeEach((to, from, next) => {
  console.log(to.meta?.access);
  if (to.meta?.access === "CanAdmit") {
    if (userStore.getRole !== "admit") {
      next("/noauth");
      return;
    }
  }
  next();
});
</script>

<template>
  <BasicLayout />
</template>
