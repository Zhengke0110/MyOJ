<template>
  <div>
    <h2>{{ route.path.includes("update") ? "修改题目" : "创建题目" }}</h2>
    <a-form
      :model="form"
      label-align="left"
      auto-label-width
      layout="horizontal"
    >
      <a-form-item field="title" label="标题">
        <a-input v-model="form.title" placeholder="请输入标题" />
      </a-form-item>

      <a-form-item field="tags" label="标签">
        <a-input-tag v-model="form.tags" placeholder="请选择标签" allow-clear />
      </a-form-item>

      <a-form-item field="content" label="题目内容">
        <MDEditor :value="form.content" :handle-change="onContentChange" />
      </a-form-item>

      <a-form-item field="answer" label="答案">
        <MDEditor :value="form.answer" :handle-change="onAnswerChange" />
      </a-form-item>

      <a-form-item label="判题配置" :content-flex="false" :merge-props="false">
        <a-row :gutter="20">
          <a-col :span="8">
            <a-card hoverable>
              <a-form-item field="judgeConfig.timeLimit" label="时间限制">
                <a-input-number
                  v-model="form.judgeConfig.timeLimit"
                  placeholder="请输入时间限制"
                  mode="button"
                  :min="0"
                  size="large"
                />
              </a-form-item>
            </a-card>
          </a-col>
          <a-col :span="8">
            <a-card hoverable>
              <a-form-item field="judgeConfig.memoryLimit" label="内存限制">
                <a-input-number
                  v-model="form.judgeConfig.memoryLimit"
                  placeholder="请输入内存限制"
                  mode="button"
                  :min="0"
                  size="large"
                />
              </a-form-item>
            </a-card>
          </a-col>
          <a-col :span="8">
            <a-card hoverable>
              <a-form-item field="judgeConfig.stackLimit" label="堆栈限制">
                <a-input-number
                  v-model="form.judgeConfig.stackLimit"
                  placeholder="请输入堆栈限制"
                  mode="button"
                  :min="0"
                  size="large"
                />
              </a-form-item>
            </a-card>
          </a-col>
        </a-row>
      </a-form-item>

      <a-form-item
        label="测试用例配置"
        :content-flex="false"
        :merge-props="false"
      >
        <a-row :gutter="20">
          <a-form-item
            v-for="(judgeCaseItem, index) of form.judgeCase"
            :key="index"
            no-style
          >
            <!-- <a-space direction="vertical" class="min-w-[480px] m-4"> -->
            <a-col :span="8" class="my-2">
              <a-card hoverable>
                <a-form-item
                  :field="`form.judgeCase[${index}].input`"
                  :label="`输入用例-${index}`"
                  :key="index"
                >
                  <a-input
                    v-model="judgeCaseItem.input"
                    placeholder="请输入测试输入用例"
                  />
                </a-form-item>
                <a-form-item
                  :field="`form.judgeCase[${index}].output`"
                  :label="`输出用例-${index}`"
                  :key="index"
                >
                  <a-input
                    v-model="judgeCaseItem.output"
                    placeholder="请输入测试输出用例"
                  />
                </a-form-item>
                <a-button status="danger" @click="handleDelete(index)">
                  删除
                </a-button>
              </a-card>
            </a-col>
            <!-- </a-space> -->
          </a-form-item>
        </a-row>
        <div style="margin-top: 32px">
          <a-button @click="handleAdd" type="outline" status="success"
            >新增测试用例
          </a-button>
        </div>
      </a-form-item>

      <a-form-item>
        <a-button
          v-if="route.path.includes('update')"
          type="primary"
          style="min-width: 200px"
          @click="doUpdateSubmit"
          >修改题目
        </a-button>
        <a-button
          v-else
          type="primary"
          style="min-width: 200px"
          @click="doCreateSubmit"
          >新建题目
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from "vue";
import { Message } from "@arco-design/web-vue";
import { type QuestionInterface, JudgeCaseItem } from "@/config";
import MDEditor from "@/components/MDEditor";
import {
  AddQuestion,
  UpdateQuestion,
  AdminGetQuestionById,
} from "@/services/question";
import { useRoute } from "vue-router";
const route = useRoute();

const form = ref<QuestionInterface>({
  title: "",
  tags: [],
  answer: "",
  content: "",
  judgeConfig: {
    memoryLimit: 1000,
    stackLimit: 1000,
    timeLimit: 1000,
  },
  judgeCase: [JudgeCaseItem],
});

const onContentChange = (value: string) => (form.value.content = value);

const onAnswerChange = (value: string) => (form.value.answer = value);

/**
 * 新增判题用例
 */
const handleAdd = () => {
  form.value.judgeCase.push(JudgeCaseItem);
};

const handleDelete = (index: number) => form.value.judgeCase.splice(index, 1);

/**
 * 提交
 */
const doCreateSubmit = async () => {
  const { data, message, code } = await AddQuestion(form.value);
  if (code === 0 && data) Message.success(`创建题目成功`);
  else Message.error(`创建题目失败, 原因: ${message}`);
};
// TODO id需要重传
const doUpdateSubmit = async () => {
  let id = "1877753623343509505";
  const { data, message, code } = await UpdateQuestion(form.value, id);
  if (code === 0 && data) Message.success(`修改题目成功`);
  else Message.error(`修改题目失败, 原因: ${message}`);
};
watch(
  () => route.path,
  (newValue) => {
    console.log("route.path", newValue);
    LoadQuestionInfo();
  }
);

// TODO id需要重传
const LoadQuestionInfo = async () => {
  let id = "1877753623343509505";

  const { data, message, code } = await AdminGetQuestionById(id);
  if (code === 0 && data) {
    const mappedData: QuestionInterface = {
      title: data.title || "",
      tags: data.tags || [],
      answer: data.answer || "",
      content: data.content || "",
      judgeConfig: {
        memoryLimit: Number(data.judgeConfig?.memoryLimit) || 0,
        stackLimit: Number(data.judgeConfig?.stackLimit) || 0,
        timeLimit: Number(data.judgeConfig?.timeLimit) || 0,
      },
      judgeCase: [],
    };

    if (typeof data.judgeCase === "string" && data.judgeCase.trim() !== "") {
      try {
        mappedData.judgeCase = JSON.parse(data.judgeCase);
      } catch (error) {
        Message.error(`Failed to parse judgeCase JSON:${error}`);
        mappedData.judgeCase = [JudgeCaseItem];
      }
    } else {
      mappedData.judgeCase = [JudgeCaseItem];
    }
    form.value = mappedData;
  } else {
    Message.error(`获取题目信息失败, 原因: ${message}`);
  }
};

onMounted(() => {
  if (route.path.includes("update")) LoadQuestionInfo();
});
</script>
