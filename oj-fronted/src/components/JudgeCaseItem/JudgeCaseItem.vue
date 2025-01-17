<template>
  <div
    class="overflow-hidden rounded-md bg-gray-50/2 border-gray-100 border border-dashed"
  >
    <div class="px-4 py-5 sm:p-6">
      <div>
        <label class="block text-sm/6 font-medium text-gray-900"
          >输入用例-{{ index }}</label
        >
        <div class="mt-3">
          <input
            type="text"
            v-model="form.input"
            class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
            placeholder="请输入测试输入用例"
          />
        </div>
      </div>
      <div class="mt-3">
        <label for="email" class="block text-sm/6 font-medium text-gray-900"
          >输出用例-{{ index }}</label
        >
        <div class="mt-3">
          <input
            type="text"
            v-model="form.output"
            class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
            placeholder="请输入测试输出用例"
          />
        </div>
      </div>
      <div class="mt-3 flex justify-end" v-if="isAdd">
        <button
          type="button"
          class="rounded bg-red-600 px-2 py-1 text-xs font-semibold text-white shadow-sm hover:bg-red-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-red-600"
          @click="handleDelete(index)"
        >
          删除
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from "vue";

interface Props {
  index: number;
  input: string;
  output: string;
  isAdd: boolean;
}

const emit = defineEmits(["onUpdateItem", "onDeleteItem"]);

const { index, input, output, isAdd } = defineProps<Props>();

const form = ref<{ input: string; output: string }>({ input, output });

const handleDelete = (index: number) => emit("onDeleteItem", index);
const handleChange = () => emit("onUpdateItem", form.value);
watch(
  () => form.value,
  () => {
    if (isAdd) handleChange();
  },
  { deep: true }
);

onMounted(() => (form.value = { input, output }));
</script>

<style scoped></style>
