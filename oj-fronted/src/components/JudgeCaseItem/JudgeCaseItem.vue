<template>
  <a-card>
    <a-form-item
      :field="`form.judgeCase[${index}].input`"
      :label="`输入用例-${index}`"
      :key="index"
    >
      <a-input
        v-model="form.input"
        :disabled="!isAdd"
        placeholder="请输入测试输入用例"
      />
    </a-form-item>
    <a-form-item
      :field="`form.judgeCase[${index}].output`"
      :label="`输出用例-${index}`"
      :key="index"
    >
      <a-input
        v-model="form.output"
        :disabled="!isAdd"
        placeholder="请输入测试输出用例"
      />
    </a-form-item>
    <a-button status="danger" v-if="isAdd" @click="handleDelete(index)">
      删除
    </a-button>
  </a-card>
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
