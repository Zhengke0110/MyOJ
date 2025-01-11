<template>
  <a-layout class="min-h-[100vh]">
    <a-layout-header class="mb-6"><GlobalHeader /></a-layout-header>
    <!-- Content -->
    <a-layout-content class="mb-6 block p-[20px]">
      <RouterView class="min-h-[500px] max-w-[1080px] mx-auto" />
    </a-layout-content>
    <!-- Footer -->
    <a-layout-footer class="flex justify-center">
      <GlobalFooter
    /></a-layout-footer>
  </a-layout>
</template>

<script setup lang="ts">
import { RouterView } from "vue-router";
import GlobalHeader from "@/components/GlobalHeader";
import GlobalFooter from "@/components/GlobalFooter";
</script>
