<template>
  <span
    class="inline-flex items-center rounded-md px-2 py-1 text-xs font-medium m-1"
    :class="[safeColor, safeBgColor]"
    >{{ text }}</span
  >
</template>

<script setup lang="ts">
import { computed } from "vue";

const { text, index = 0 } = defineProps<{
  text: string;
  index?: number;
}>();

const colors = [
  "text-red-700 ",
  "text-orange-800 ",
  "text-yellow-800 ",
  "text-green-700 ",
  "text-blue-700 ",
  "text-indigo-700 ",
  "text-purple-700 ",
];

const bgColors = [
  "bg-red-100",
  "bg-orange-100",
  "bg-yellow-100",
  "bg-green-100",
  "bg-blue-100",
  "bg-indigo-100",
  "bg-purple-100",
];

const safeColor = computed(() => colors[index % bgColors.length]);
const safeBgColor = computed(() => bgColors[index % bgColors.length]);
</script>

<style scoped></style>
