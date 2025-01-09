<template>
  <div>
    noauth

    <!-- <MDEditor :value="mdValue" :handle-change="onMdChange" /> -->
    <CodeEditor
      :value="codeValue"
      :handle-change="onCodeChange"
      class="min-h-[400px]"
    />
    <button @click="console.log(codeValue)">提交</button>
  </div>
</template>

<script setup lang="ts">
import MDEditor from "@/components/MDEditor";
import CodeEditor from "@/components/CodeEditor";
import { ref } from "vue";

const mdValue = ref();
const codeValue = ref();

const onMdChange = (v: string) => (mdValue.value = v);

const onCodeChange = (v: string) => (codeValue.value = v);
</script>
