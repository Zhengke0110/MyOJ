<template>
  <Editor
    :value="value"
    :mode="mode"
    :plugins="plugins"
    @change="handleChange"
    :locale="zhHans"
  />
</template>

<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Editor, Viewer } from "@bytemd/vue-next";
import zhHans from "bytemd/lib/locales/zh_Hans.json";
import "bytemd/dist/index.css";
// import "juejin-markdown-themes/dist/juejin.min.css"; // 掘金同款样式

/**
 * 定义组件属性类型
 */
interface Props {
  value: string;
  mode?: string;
  handleChange?: (v: string) => void;
}

const plugins = [
  gfm(),
  highlight(),
  // Add more plugins here
];

/**
 * 给组件指定初始值
 */

const {
  value = "",
  mode = "split",
  handleChange = (v: string) => console.log(v),
} = defineProps<Props>();
</script>

<style>
.bytemd-toolbar-icon.bytemd-tippy.bytemd-tippy-right:nth-last-child(-n + 2) {
  display: none;
}

.bytemd {
  width: 65vw !important;
  min-height: 480px !important;
}
</style>
