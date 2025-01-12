<!-- 管理员可见 -->
<template>
  <div class="w-full">
    <a-table
      :columns="QuestionAdminTableColumns"
      :data="QuestionInfo"
      :loading="isLoading"
      :pagination="{
        showTotal: true,
        pageSize: PageInfo.pageSize,
        current: PageInfo.current,
        total,
      }"
      @page-change="onPageChange"
    >
      <template #tags="{ record }">
        <div>
          <Badges
            v-for="(item, index) in record.tags"
            :text="item as string"
            :index="index"
          />
        </div>
      </template>

      <template #acceptedRate="{ record }">
        <div>
          {{
            `${
              record.submitNum ? record.acceptedNum / record.submitNum : "0"
            }% (${record.acceptedNum}/${record.submitNum})`
          }}
        </div>
      </template>

      <template #action="{ record }">
        <div>
          <button
            type="button"
            class="inline-flex items-center gap-x-1.5 rounded-md bg-indigo-600 px-2.5 py-1.5 mx-1 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            @click="detailQuestionAction(record.id as string)"
          >
            详情
            <i class="i-tabler:list-details size-5" />
          </button>

          <button
            type="button"
            class="inline-flex items-center gap-x-1.5 rounded-md bg-green-600 px-2.5 py-1.5 mx-1 text-sm font-semibold text-white shadow-sm hover:bg-green-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-green-600"
            @click="editorQuestionAction(record.id as string)"
          >
            编辑
            <i class="i-tabler:edit size-5" />
          </button>

          <button
            type="button"
            class="inline-flex items-center gap-x-1.5 rounded-md bg-red-600 px-2.5 py-1.5 mx-1 text-sm font-semibold text-white shadow-sm hover:bg-red-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-red-600"
            @click="
              deleteQuestionAction(record.id as string, record.title as string)
            "
          >
            删除
            <i class="i-tabler:layout-grid-remove" size-5 />
          </button>
        </div>
      </template>
    </a-table>
    <a-modal v-model:visible="visible" @ok="deleteOk" @cancel="deleteCancel">
      <template #title> Title </template>
      <div>
        You can customize modal body text by the current situation. This modal
        will be closed immediately once you press the OK button.
      </div>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import { Message, Modal } from "@arco-design/web-vue";
import { useRouter } from "vue-router";
import { type QuestionInterface, QuestionAdminTableColumns } from "@/config";
import { GetQuestionsAdmin, DeleteQuestionById } from "@/services/question";
import Badges from "@/components/Badges";

const total = ref(0); // 题目总数
const isLoading = ref<boolean>(true);

// 分页请求数据
const PageInfo = ref<{ current: number; pageSize: number }>({
  current: 1,
  pageSize: 20,
});
const QuestionInfo = ref<QuestionInterface[]>([]);

/**
 * 加载题目信息
 */
const LoadQuestionsInfo = async () => {
  const { data, code, message } = await GetQuestionsAdmin(
    PageInfo.value.current,
    PageInfo.value.pageSize
  );

  if (code === 0 && data) {
    total.value = Number(data.total) ?? 0;
    QuestionInfo.value = Array.isArray(data.records)
      ? data.records.map((item: any) => ({
          ...item,
          id: item.id !== undefined ? String(item.id) : undefined,
          tags: item.tags !== undefined ? JSON.parse(item.tags) : [],
        }))
      : [];
  } else Message.error(`获取题目失败, 原因: ${message}`);
  isLoading.value = false;
};
const onPageChange = (page: number) => {
  PageInfo.value = {
    ...PageInfo.value,
    current: page,
  };
  isLoading.value = true;
};

const router = useRouter();
// 编辑操作
const editorQuestionAction = (id: string) =>
  router.push({
    path: "/update/question",
    query: {
      id,
    },
  });

// 删除操作
const deleteInfo = ref<{
  id: string;
  title: string;
}>();
const deleteQuestionAction = (id: string, title: string) => {
  visible.value = true;
  deleteInfo.value = { id, title };
};

// 删除弹窗
const visible = ref(false);

const deleteOk = async () => {
  if (!deleteInfo.value?.id) {
    Message.error("无效的题目ID");
    visible.value = false;
    return;
  }

  const { data, code, message } = await DeleteQuestionById(deleteInfo.value.id);
  if (code === 0 && data) {
    Message.success("删除成功");
    visible.value = false;
    LoadQuestionsInfo(); // 刷新数据
  } else {
    Message.error(`删除失败, 原因: ${message}`);
  }
};
const deleteCancel = () => {
  visible.value = false;
  deleteInfo.value = { id: "", title: "" };
};
// 查看题目详情
const detailQuestionAction = (id: string) =>
  router.push({
    path: "/detail/question",
    query: {
      id,
    },
  });
watchEffect(() => LoadQuestionsInfo());

onMounted(() => LoadQuestionsInfo());
</script>
