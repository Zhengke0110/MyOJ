import * as monaco from "monaco-editor";

export function CreateEditor(
  container: HTMLElement,
  options?: monaco.editor.IStandaloneEditorConstructionOptions
): monaco.editor.IStandaloneCodeEditor {
  return monaco.editor.create(container, options);
}

export enum LanguageEnum {
  Java = "java",
  Cpp = "c++",
  GoLang = "golang",
}

export const DefaultOptions: monaco.editor.IStandaloneEditorConstructionOptions =
  {
    automaticLayout: true, // 自动布局
    colorDecorators: true, // 颜色装饰器
    readOnly: false, // 控制编辑器是否只读
    autoClosingOvertype: "auto", // 自动闭合括号或引号
    autoIndent: "full", // 自动缩进
    theme: "vs", // 主题
    fontSize: 16,
    folding: true, // 是否折叠
    foldingHighlight: true, // 折叠等高线
    foldingStrategy: "indentation", // 折叠方式  auto | indentation
    showFoldingControls: "mouseover", // 是否一直显示折叠 always | mouseover
    disableLayerHinting: true, // 等宽优化
    emptySelectionClipboard: false, // 空选择剪切板
    selectionClipboard: false, // 选择剪切板
    codeLens: false, // 代码镜头
    scrollBeyondLastLine: false, // 滚动完最后一行后再滚动一屏幕
    accessibilitySupport: "off", // 辅助功能支持  "auto" | "off" | "on"
    lineNumbers: "on", // 行号 取值： "on" | "off" | "relative" | "interval" | function
    lineNumbersMinChars: 1, // 行号最小字符   number
    cursorStyle: "line", //光标样式
    glyphMargin: true, //字形边缘
    useTabStops: false,
  };
