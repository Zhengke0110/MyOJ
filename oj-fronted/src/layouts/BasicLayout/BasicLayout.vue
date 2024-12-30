<template>
  <a-layout>
    <a-layout-header class="mb-8 bg-cyan"><GlobalHeader /></a-layout-header>
    <!-- Content -->
    <a-layout-content class="mb-8 bg-fuchsia">
      <RouterView />
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
