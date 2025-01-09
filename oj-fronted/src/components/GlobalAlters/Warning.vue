<template>
  <div v-if="isVisible" class="rounded-md bg-yellow-50 p-4">
    <div class="flex justify-center items-center">
      <div class="shrink-0">
        <i
          class="i-tabler:alert-circle size-5 text-yellow-400"
          aria-hidden="true"
        />
      </div>
      <div class="ml-3" :class="center ? 'text-center' : 'text-left'">
        <p class="text-sm font-medium text-yellow-800">{{ info }}</p>
      </div>
      <div class="ml-auto pl-3">
        <div class="-mx-1.5 -my-1.5">
          <button
            v-show="closable"
            type="button"
            class="inline-flex rounded-md bg-yellow-50 p-1.5 text-yellow-500 hover:bg-yellow-100 focus:outline-none focus:ring-2 focus:ring-yellow-600 focus:ring-offset-2 focus:ring-offset-yellow-50"
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
import type { Props } from "./alter-config";
const {
  info = "Warning",
  center = true,
  closable = true,
} = defineProps<Props>();
const isVisible = ref<boolean>(true);
</script>
