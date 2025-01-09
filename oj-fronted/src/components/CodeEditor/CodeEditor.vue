<template>
  <div ref="editorRef" class="w-full h-full"></div>
</template>

<script lang="ts" setup>
import { ref, onMounted, toRaw, onUnmounted } from "vue";
import jsonWorker from "monaco-editor/esm/vs/language/json/json.worker?worker";
import cssWorker from "monaco-editor/esm/vs/language/css/css.worker?worker";
import htmlWorker from "monaco-editor/esm/vs/language/html/html.worker?worker";
import tsWorker from "monaco-editor/esm/vs/language/typescript/ts.worker?worker";
import EditorWorker from "monaco-editor/esm/vs/editor/editor.worker?worker";
import { DefaultOptions, CreateEditor } from "./code-editor.config";
interface Props {
  value?: string;
  handleChange?: (v: string) => void;
  language?: string;
}
const codeEditor = ref();
const {
  value = "",
  language = "java",
  handleChange = (v: string) => console.log(v),
} = defineProps<Props>();
// 使用 ref 创建一个 DOM 元素的引用
const editorRef = ref(null);

onMounted(() => {
  // 初始化 Monaco Editor
  if (!editorRef.value) return;
  codeEditor.value = CreateEditor(editorRef.value, {
    value: value,
    language: language,
    ...DefaultOptions,
  });

  // 编辑 监听内容变化
  codeEditor.value.onDidChangeModelContent(() => {
    handleChange(toRaw(codeEditor.value).getValue());
  });
});

onUnmounted(() => {
  console.log("Code Editor组件卸载");
  // 检查 editor 是否已初始化
  if (codeEditor.value) codeEditor.value.dispose(); // 清理 editor 资源，避免内存泄漏
});

// 解决爆内存问题
self.MonacoEnvironment = {
  getWorker(_: string, label: string) {
    if (label === "json") {
      return new jsonWorker();
    }
    if (label === "css" || label === "scss" || label === "less") {
      return new cssWorker();
    }
    if (label === "html" || label === "handlebars" || label === "razor") {
      return new htmlWorker();
    }
    if (["typescript", "javascript"].includes(label)) {
      return new tsWorker();
    }
    return new EditorWorker();
  },
};
</script>
