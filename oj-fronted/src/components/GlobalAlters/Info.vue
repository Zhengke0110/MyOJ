<template>
  <div v-if="isVisible" class="rounded-md bg-blue-50 p-4">
    <div class="flex justify-center items-center">
      <div class="shrink-0">
        <i
          class="i-tabler:info-circle size-5 text-blue-400"
          aria-hidden="true"
        />
      </div>
      <div class="ml-3" :class="center ? 'text-center' : 'text-left'">
        <p class="text-sm font-medium text-blue-800">{{ info }}</p>
      </div>
      <div class="ml-auto pl-3">
        <div class="-mx-1.5 -my-1.5">
          <button
            v-show="closable"
            type="button"
            class="inline-flex rounded-md bg-blue-50 p-1.5 text-blue-500 hover:bg-blue-100 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-blue-50"
            @click="isVisible = false"
          >
            <i class="i-tabler:square-letter-x size-5" />
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import type { AltersProps } from "./alter-config";
const {
  info = "Info",
  center = true,
  closable = true,
} = defineProps<AltersProps>();
const isVisible = ref<boolean>(true);
</script>
