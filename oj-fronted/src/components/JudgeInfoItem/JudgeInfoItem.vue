<template>
  <div
    class="overflow-hidden rounded-md bg-gray-50/2 border-gray-100 border border-dashed inline-flex"
  >
    <div class="px-4 py-5 sm:p-6">
      <label class="block text-sm/6 mb-2 font-medium text-gray-900">{{
        lable
      }}</label>

      <div class="flex justify-center items-center">
        <input
          type="number"
          :value="value"
          class="flex-1 mx-1 h-8 bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md pl-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow appearance-none [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
        />
        <button
          class="rounded mx-1 w-8 h-8 bg-slate-800 p-1.5 border border-transparent text-center text-sm text-white transition-all shadow-sm hover:shadow focus:bg-slate-700 focus:shadow-none active:bg-slate-700 hover:bg-slate-700 active:shadow-none disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"
          type="button"
          @click="decreased"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 16 16"
            fill="currentColor"
            class="w-4 h-4"
          >
            <path
              d="M3.75 7.25a.75.75 0 0 0 0 1.5h8.5a.75.75 0 0 0 0-1.5h-8.5Z"
            />
          </svg>
        </button>
        <button
          id=""
          class="rounded mx-1 w-8 h-8 bg-slate-800 p-1.5 border border-transparent text-center text-sm text-white transition-all shadow-sm hover:shadow focus:bg-slate-700 focus:shadow-none active:bg-slate-700 hover:bg-slate-700 active:shadow-none disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"
          type="button"
          @click="increased"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 16 16"
            fill="currentColor"
            class="w-4 h-4"
          >
            <path
              d="M8.75 3.75a.75.75 0 0 0-1.5 0v3.5h-3.5a.75.75 0 0 0 0 1.5h3.5v3.5a.75.75 0 0 0 1.5 0v-3.5h3.5a.75.75 0 0 0 0-1.5h-3.5v-3.5Z"
            />
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const { lable = "", value = 0 } = defineProps<{
  lable: string;
  value: number;
}>();
const emits = defineEmits(["increased", "decreased"]);
const increased = () => emits("increased", value + 1);
const decreased = () => emits("decreased", value - 1);
</script>
