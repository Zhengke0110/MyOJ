<template>
  <div>
    <a-menu mode="horizontal" :default-selected-keys="['1']">
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
      <a-menu-item key="1">Home</a-menu-item>
      <a-menu-item key="2">Solution</a-menu-item>
      <a-menu-item key="3">Cloud Service</a-menu-item>
      <a-menu-item key="4">Cooperation</a-menu-item>
    </a-menu>
  </div>
</template>

<script setup lang="ts">
import { LogoPath } from "@/config";
</script>
