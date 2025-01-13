<template>
  <div class="w-full">
    <a-tabs default-active-key="question">
      <a-tab-pane key="question" title="题目">
        <a-card v-if="QuestionInfo" :title="QuestionInfo.title">
          <a-descriptions title="判题条件" :column="{ xs: 1, md: 2, lg: 3 }">
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

      <a-tab-pane key="case" title="判题用例">
        <a-form v-if="QuestionInfo" :model="QuestionInfo">
          <a-row :gutter="20">
            <a-form-item
              v-for="(judgeCaseItem, index) of QuestionInfo.judgeCase"
              :key="index"
              no-style
            >
              <a-col :span="8" class="my-2">
                <JudgeCaseItem
                  :index="index"
                  :input="judgeCaseItem.input || ''"
                  :output="judgeCaseItem.output || ''"
                  :isAdd="false"
                />
              </a-col>
            </a-form-item>
          </a-row>
        </a-form>
      </a-tab-pane>

      <a-tab-pane key="user" title="题目所属者">
        <div
          class="mx-auto mt-20 grid max-w-2xl grid-cols-1 gap-x-8 gap-y-16 sm:grid-cols-2 lg:mx-0 lg:max-w-none lg:grid-cols-3"
        >
          <div>
            <img
              class="aspect-3/2 w-full rounded-2xl object-cover"
              :src="CreateUserInfo?.userAvatar"
              alt=""
            />
            <h3
              class="mt-6 text-lg/8 font-semibold tracking-tight text-gray-900"
            >
              用户角色: {{ CreateUserInfo?.userRole }}
            </h3>
          </div>

          <div class="mx-auto max-w-2xl lg:mx-0">
            <h2
              class="text-4xl font-semibold tracking-tight text-pretty text-gray-900 sm:text-5xl"
            >
              {{ CreateUserInfo?.userName }}
            </h2>
            <p class="mt-6 text-lg/8 text-gray-600">
              {{ CreateUserInfo?.userProfile }}
            </p>
          </div>
        </div>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import {
  JudgeCaseItemInfo,
  type QuestionInterface,
  type UserInterface,
} from "@/config";
import { Message } from "@arco-design/web-vue";
import { AdminGetQuestionById } from "@/services/question";
import Badges from "@/components/Badges";
import MDViewer from "@/components/MDViewer";
import JudgeCaseItem from "@/components/JudgeCaseItem";

const router = useRouter();
const route = useRoute();
const QuestionInfo = ref<QuestionInterface | null>(null);
const CreateUserInfo = ref<UserInterface | null>(null);
const LoadQuestionInfo = async (id: string) => {
  const { data, code, message } = await AdminGetQuestionById(id);

  if (code === 0 && data) {
    console.log(data.userVO);
    // 确保 userVO 存在并转换为 UserInterface 类型
    CreateUserInfo.value = data.userVO
      ? {
          id: String(data.userVO.id || ""),
          userName: data.userVO.userName || "",
          userAvatar: data.userVO.userAvatar || "",
          userRole: data.userVO.userRole || "",
          userProfile: data.userVO.userProfile || "",
        }
      : null;

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
    };

    if (typeof data.judgeCase === "string" && data.judgeCase.trim() !== "") {
      try {
        mappedData.judgeCase = JSON.parse(data.judgeCase);
      } catch (error) {
        Message.error(`Failed to parse judgeCase JSON:${error}`);
        mappedData.judgeCase = [JudgeCaseItemInfo];
      }
    } else {
      mappedData.judgeCase = [JudgeCaseItemInfo];
    }

    QuestionInfo.value = mappedData;
  } else Message.error(`获取题目失败, 原因: ${message}`);
};

onMounted(() => {
  if (route.query.id) LoadQuestionInfo(route.query.id as string);
});
</script>

<style scoped></style>
