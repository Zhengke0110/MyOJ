<template>
  <Codemirror
    v-model:value="code"
    :options="cmOptions"
    class="w-full min-h-[360px] h-full"
    :KeepCursorInEnd="true"
    :border="false"
    @ready="onReady"
    @input="handleChange"
  />
</template>
<script lang="ts" setup>
import { ref, reactive, onMounted, computed, watch } from "vue";
import type { Editor, EditorConfiguration } from "codemirror";
import Codemirror from "codemirror-editor-vue3";
// mode
import "codemirror/mode/clike/clike.js";
import "codemirror/mode/go/go.js";

const code = ref(DefaultJavaContent);
import {
  DefaultJavaContent,
  DefaultGoContent,
  DefaultCppContent,
} from "./code-editor.config";

interface Props {
  value?: string;
  handleChange?: (v: string) => void;
  language?: string;
}
const {
  value,
  language = "java",
  handleChange = (v: string) => console.log(v),
} = defineProps<Props>();

enum Language {
  java = "text/x-java",
  cpp = "text/x-c++src",
  go = "text/x-go",
}

const cmOptions: EditorConfiguration = reactive({
  mode: computed(
    () => Language[language as keyof typeof Language] || "text/x-java"
  ),
  theme: "default",
  readOnly: false,
  lineNumbers: true,
  lineWiseCopyCut: true,
  gutters: ["CodeMirror-lint-markers"],
  lint: true,
  tabSize: 4,
  lineWrapping: true,
  scrollbarStyle: "null",
  autofocus: true,
  cursorHeight: 1,
  electricChars: true,
  autoCloseBrackets: true,
  matchBrackets: true,
  styleActiveLine: true,
});

const cminstance = ref<Editor | null>(null);
const onReady = (cm: Editor) => (cminstance.value = cm);

const selectContent = () => {
  switch (language) {
    case "go":
      code.value = DefaultGoContent;
      break;
    case "java":
      code.value = DefaultJavaContent;
      break;
    case "cpp":
      code.value = DefaultCppContent;
      break;
    default:
      code.value = DefaultJavaContent;
  }
};
onMounted(() => {
  if (value && value.length > 0) code.value = value;
  else selectContent;
});

watch(
  () => language,
  () => selectContent()
);
</script>

<style lang="css">
.CodeMirror-wrap pre.CodeMirror-line,
.CodeMirror-wrap pre.CodeMirror-line-like {
  line-height: 24px;
}
</style>
