<template>
  <span
    class="inline-flex items-center rounded-md px-1.5 py-0.5 text-xs font-bold m-0.5"
    :class="[safeColor, safeBgColor]"
  >
    {{ text }}
    <button
      type="button"
      class="size-3 rounded-sm ml-2"
      :class="hoverBgColors"
      @click="emits('remove', index)"
    >
      <span class="sr-only">Remove</span>
      <i class="i-tabler:letter-x size-3" :class="[safeStrokeColor]"></i>
    </button>
  </span>
</template>

<script setup lang="ts">
import { computed } from "vue";
const { text, index = 0 } = defineProps<{
  text: string;
  index?: number;
}>();

const emits = defineEmits(["remove"]);
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
const hoverBgColors = [
  "hover:bg-red-600/20",
  "hover:bg-orange-600/20",
  "hover:bg-yellow-600/20",
  "hover:bg-green-600/20",
  "hover:bg-blue-600/20",
  "hover:bg-indigo-600/20",
  "hover:bg-purple-600/20",
];
const strokeColors = [
  "text-red-700/50",
  "text-orange-800/50",
  "text-yellow-800/50",
  "text-green-700/50",
  "text-blue-700/50",
  "text-indigo-700/50",
  "text-purple-700/50",
];

const safeColor = computed(() => colors[index % colors.length]);
const safeBgColor = computed(() => bgColors[index % bgColors.length]);
const safeHoverBgColor = computed(
  () => hoverBgColors[index % hoverBgColors.length]
);
const safeStrokeColor = computed(
  () => strokeColors[index % strokeColors.length]
);
</script>
