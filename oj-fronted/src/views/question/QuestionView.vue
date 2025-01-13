<template>
  <div>
    <h2>{{ isUpdateMode ? "修改题目" : "创建题目" }}</h2>
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
        <MDEditor
          :value="form.answer ? form.answer : ''"
          :handle-change="onAnswerChange"
        />
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
            <a-col :span="8" class="my-2">
              <!-- <a-card hoverable>
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
              </a-card> -->
              <JudgeCaseItem
                :index="index"
                :input="judgeCaseItem.input"
                :output="judgeCaseItem.output"
                :isAdd="true"
                @onUpdateItem="handleUpdateItem(index, $event)"
                @onDeleteItem="handleDelete(index)"
              />
            </a-col>
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
          v-if="isUpdateMode"
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
import { computed, onMounted, ref, watch } from "vue";
import { Message } from "@arco-design/web-vue";
import { type QuestionInterface, JudgeCaseItemInfo } from "@/config";
import MDEditor from "@/components/MDEditor";
import {
  AddQuestion,
  UpdateQuestion,
  AdminGetQuestionById,
} from "@/services/question";
import { useRoute } from "vue-router";
import JudgeCaseItem from "@/components/JudgeCaseItem";
const route = useRoute();

const form = ref<QuestionInterface>({
  id: "",
  title: "",
  tags: [],
  answer: "",
  content: "",
  judgeConfig: {
    memoryLimit: 1000,
    stackLimit: 1000,
    timeLimit: 1000,
  },
  judgeCase: [JudgeCaseItemInfo],
});

const onContentChange = (value: string) => (form.value.content = value);

const onAnswerChange = (value: string) => (form.value.answer = value);

// 处理更新测试用例的方法
const handleUpdateItem = (
  index: number,
  updatedItem: { input: string; output: string }
) => {
  if (
    form.value.judgeCase &&
    index >= 0 &&
    index < form.value.judgeCase.length
  ) {
    form.value.judgeCase[index] = updatedItem;
  } else {
    Message.error("无效的索引或测试用例为空");
  }
};

/**
 * 新增判题用例
 */
const handleAdd = () => {
  if (Array.isArray(form.value.judgeCase)) {
    form.value.judgeCase.push({ ...JudgeCaseItemInfo });
  } else {
    form.value.judgeCase = [{ ...JudgeCaseItemInfo }];
  }
};

const handleDelete = (index: number) => {
  if (
    form.value.judgeCase &&
    index >= 0 &&
    index < form.value.judgeCase.length
  ) {
    form.value.judgeCase.splice(index, 1);
  } else {
    Message.error("无效的索引或测试用例为空");
  }
};

/**
 * 提交
 */
const doCreateSubmit = async () => {
  const { data, message, code } = await AddQuestion(form.value);
  if (code === 0 && data) Message.success(`创建题目成功`);
  else Message.error(`创建题目失败, 原因: ${message}`);
};

const doUpdateSubmit = async () => {
  if (!form.value.id) {
    Message.error("题目ID不能为空");
    return;
  }

  const { data, message, code } = await UpdateQuestion(
    form.value,
    form.value.id
  );
  if (code === 0 && data) Message.success(`修改题目成功`);
  else Message.error(`修改题目失败, 原因: ${message}`);
};

watch(
  () => route.path,
  () => {
    form.value = {
      id: "",
      title: "",
      tags: [],
      answer: "",
      content: "",
      judgeConfig: {
        memoryLimit: 1000,
        stackLimit: 1000,
        timeLimit: 1000,
      },
      judgeCase: [JudgeCaseItemInfo],
    };
    if (route.path.includes("update") && route.query.id)
      LoadQuestionInfo(route.query.id as string);
  }
);

// 根据ID加载数据
const LoadQuestionInfo = async (id: string) => {
  const { data, message, code } = await AdminGetQuestionById(id);
  if (code === 0 && data) {
    const mappedData: QuestionInterface = {
      id: String(data.id) || "",
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
        mappedData.judgeCase = [JudgeCaseItemInfo];
      }
    } else {
      mappedData.judgeCase = [JudgeCaseItemInfo];
    }
    form.value = mappedData;
  } else {
    Message.error(`获取题目信息失败, 原因: ${message}`);
  }
};

onMounted(() => {
  if (route.path.includes("update") && route.query.id)
    LoadQuestionInfo(route.query.id as string);
});

const isUpdateMode = computed(() => route.path.includes("update"));
</script>
