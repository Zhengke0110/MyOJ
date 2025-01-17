<!-- 管理员可见 -->
<template>
  <div class="w-full">
    <table class="min-w-full divide-y divide-gray-300">
      <thead>
        <tr>
          <th
            v-for="item in QuestionAdminTableColumns"
            scope="col"
            class="sticky top-0 z-10 border-b border-gray-300 bg-white/75 px-3 py-3.5 text-left text-sm font-semibold text-gray-900"
          >
            {{ item.title }}
          </th>
        </tr>
      </thead>
      <tbody class="bg-white">
        <tr v-for="item in QuestionInfo" :key="item.id" class="even:bg-gray-50">
          <td
            class="whitespace-nowrap py-4 pl-4 px-3 text-sm font-medium text-gray-900 sm:pl-3"
          >
            {{ item.id }}
          </td>
          <td class="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
            {{ item.title }}
          </td>
          <td class="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
            <Badges
              v-for="(i, index) in item.tags"
              :text="i as string"
              :index="index"
            ></Badges>
          </td>
          <td class="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
            {{
              `${item.submitNum ? item.acceptedNum / item.submitNum : "0"}% (${
                item.acceptedNum
              }/${item.submitNum})`
            }}
          </td>
          <td class="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
            <button
              type="button"
              class="inline-flex items-center gap-x-1.5 rounded-md bg-indigo-600 px-2.5 py-1.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
              @click="detailQuestionAction(item.id as string)"
            >
              详情
              <i class="i-tabler:list-details size-5" />
            </button>
          </td>
          <td class="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
            <button
              type="button"
              class="inline-flex items-center gap-x-1.5 rounded-md bg-green-600 px-2.5 py-1.5 text-sm font-semibold text-white shadow-sm hover:bg-green-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-green-600"
              @click="editorQuestionAction(item.id as string)"
            >
              编辑
              <i class="i-tabler:edit size-5" />
            </button>
          </td>
          <td class="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
            <button
              type="button"
              class="inline-flex items-center gap-x-1.5 rounded-md bg-red-600 px-2.5 py-1.5 text-sm font-semibold text-white shadow-sm hover:bg-red-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-red-600"
              @click="
                deleteQuestionAction(item.id as string, item.title as string)
              "
            >
              删除
              <i class="i-tabler:layout-grid-remove" size-5 />
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <Pagination
      :total="total"
      :current="PageInfo.current"
      :page-size="PageInfo.pageSize"
      @previous="PreviousHandle"
      @next="NextHandle"
      @select-page="SelectPageHandle"
    />
    <ModalDialogs
      :open="deleteModalDialogsInfo.visible"
      :title="deleteModalDialogsInfo.title"
      :content="deleteModalDialogsInfo.content"
      @cancel="cancelHandle"
      @confirm="confirmHandle"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";
import { type QuestionInterface, QuestionAdminTableColumns } from "@/config";
import { GetQuestionsAdmin, DeleteQuestionById } from "@/services/question";
import Badges from "@/components/Badges";
import Pagination from "@/components/Pagination";
import ModalDialogs from "@/components/ModalDialogs";

const total = ref<number>(0); // 题目总数
const isLoading = ref<boolean>(true);

// 分页请求数据
const PageInfo = ref<{ current: number; pageSize: number }>({
  current: 1,
  pageSize: 20,
});
const QuestionInfo = ref<QuestionInterface[]>([]);

const PreviousHandle = (current: number) =>
  (PageInfo.value = { ...PageInfo.value, current: current });

const NextHandle = (current: number) =>
  (PageInfo.value = { ...PageInfo.value, current: current });

const SelectPageHandle = (current: number) =>
  (PageInfo.value = { ...PageInfo.value, current: current });

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
const deleteModalDialogsInfo = ref<{
  title: string;
  content: string;
  visible: boolean;
}>({
  title: "",
  content: "📢 请注意,此操作将会永久的删除题目信息,无法挽回,请三思",
  visible: false,
});

const deleteQuestionAction = (id: string, title: string) => {
  deleteModalDialogsInfo.value.title = `是否删除题目: ${title}`;
  deleteModalDialogsInfo.value.visible = true;
  deleteInfo.value = { id, title };
};

// 删除弹窗
const cancelHandle = () => (deleteModalDialogsInfo.value.visible = false);
const confirmHandle = async () => {
  console.log("confirm");
  if (!deleteInfo.value?.id) {
    Message.error("无效的题目ID");
    deleteModalDialogsInfo.value.visible = false;

    return;
  }

  const { data, code, message } = await DeleteQuestionById(deleteInfo.value.id);
  if (code === 0 && data) {
    Message.success("删除成功");
    deleteModalDialogsInfo.value.visible = false;
    LoadQuestionsInfo(); // 刷新数据
  } else {
    Message.error(`删除失败, 原因: ${message}`);
  }
};

// 查看题目详情
const detailQuestionAction = (id: string) =>
  router.push({
    path: "/detail/question",
    query: { id },
  });
watchEffect(() => LoadQuestionsInfo());
</script>
