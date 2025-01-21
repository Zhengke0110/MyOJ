<template>
  <div class="animated animated-duration-500 animated-fade-in">
    <a-row :gutter="[24, 24]">
      <!-- Left -->
      <a-col :md="12" :xs="24">
        <a-tabs default-active-key="question">
          <a-tab-pane key="question" title="题目">
            <a-card v-if="QuestionInfo" :title="QuestionInfo.title">
              <a-descriptions
                title="判题条件"
                :column="{ xs: 1, md: 2, lg: 3 }"
              >
                <a-descriptions-item label="时间限制">
                  {{ QuestionInfo.judgeConfig.timeLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="内存限制">
                  {{ QuestionInfo.judgeConfig.memoryLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="堆栈限制">
                  {{ QuestionInfo.judgeConfig.stackLimit ?? 0 }}
                </a-descriptions-item>
              </a-descriptions>
              <a-divider />

              <!-- MD显示器 -->
              <MDViewer :value="QuestionInfo.content || ''" />
              <template #extra>
                <Badges
                  v-for="(item, index) of QuestionInfo.tags"
                  :text="item"
                  :index="index"
                />
              </template>
            </a-card>
          </a-tab-pane>
        </a-tabs>
      </a-col>
      <!-- Right -->

      <a-col :md="12" :xs="24">
        <a-form :model="QuestionSolutionForm" layout="inline">
          <a-form-item
            field="language"
            label="编程语言"
            style="min-width: 240px"
          >
            <a-select
              v-model="QuestionSolutionForm.language"
              :style="{ width: '320px' }"
              placeholder="选择编程语言"
            >
              <a-option
                v-for="(item, index) in LanguageEnum"
                :key="index"
                :value="item"
              >
                {{ item }}
              </a-option>
            </a-select>
          </a-form-item>
        </a-form>

        <CodeEditor
          :value="QuestionSolutionForm.code as string"
          :language="QuestionSolutionForm.language"
          :handle-change="changeCode"
        />
        <a-divider />

        <div class="w-full flex justify-end">
          <button
            type="button"
            class="inline-flex items-center min-w-[120px] flex justify-center gap-x-1.5 rounded-md bg-green-600 px-3 py-2 text-sm font-semibold text-white shadow-xs hover:bg-green-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-green-600"
            @click="doSubmit"
          >
            提交运行
            <i class="i-tabler:git-commit size-5"></i>
          </button>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

import { GetQuestionByID, SetQuestionSubmit } from "@/services/question";

import {
  type QuestionInterface,
  type QuestionSolutionInterface,
  DefaultLanguage,
} from "@/config";
import { Message } from "@arco-design/web-vue";
import MDViewer from "@/components/MDViewer";
import Badges from "@/components/Badges";
import CodeEditor, { LanguageEnum } from "@/components/CodeEditor";
import { useTimeoutFn } from "@vueuse/core";

const QuestionSolutionForm = ref<QuestionSolutionInterface>({
  language: DefaultLanguage,
  code: "",
  questionId: "",
});

const changeCode = (v: string) => (QuestionSolutionForm.value.code = v);

const QuestionInfo = ref<QuestionInterface | null>(null);
const LoadQuestionInfo = async (id: string) => {
  const { data, code, message } = await GetQuestionByID(id);

  if (code === 0 && data) {
    const mappedData: QuestionInterface = {
      id: String(data.id) || "",
      title: data.title || "",
      tags: data.tags || [],
      content: data.content || "",
      judgeConfig: {
        memoryLimit: Number(data.judgeConfig?.memoryLimit) || 0,
        stackLimit: Number(data.judgeConfig?.stackLimit) || 0,
        timeLimit: Number(data.judgeConfig?.timeLimit) || 0,
      },
      acceptedNum: Number(data.acceptedNum) || 0, // 添加缺失的属性
      submitNum: Number(data.submitNum) || 0, // 添加缺失的属性
    };

    QuestionInfo.value = mappedData;
  } else Message.error(`获取题目失败, 原因: ${message}`);
};
const router = useRouter();
const doSubmit = async () => {
  if (!QuestionInfo.value || !QuestionInfo.value.id) {
    Message.error("题目信息未加载或无效");
    return;
  }
  const form: QuestionSolutionInterface = {
    language: QuestionSolutionForm.value.language ?? DefaultLanguage,
    code: QuestionSolutionForm.value.code,
    questionId: QuestionInfo.value.id,
  };

  const { data, code, message } = await SetQuestionSubmit(form);
  if (code === 0 && data) Message.success(`提交成功, 即将跳转到题目列表`);
  else Message.error(`提交失败, 原因: ${message}`);
  useTimeoutFn(() => router.push("/topics"), 500);
};

const route = useRoute();
onMounted(() => {
  if (route.query.id) {
    LoadQuestionInfo(route.query.id as string);
    QuestionSolutionForm.value.questionId = route.query.id as string;
  }
});
</script>

<style scoped></style>
