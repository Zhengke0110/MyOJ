<!-- 浏览题目 - 非管理员可见 -->
<template>
  <div class="w-full">
    <a-table
      :columns="QuestionTableColumns"
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
          ></Badges>
        </div>
      </template>

      <template #action="{ record }">
        <div>
          <button
            type="button"
            class="inline-flex items-center gap-x-1.5 rounded-md bg-indigo-600 px-2.5 py-1.5 mx-1 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
          >
            <i
              class="i-tabler:brand-google-filled size-5"
              @click="go2Solution(record.id as string)"
            />
          </button>
        </div>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { type QuestionInterface, QuestionTableColumns } from "@/config";
import { GetQuestions } from "@/services/question";
import { Message } from "@arco-design/web-vue";
import { onMounted, ref, watchEffect } from "vue";
import Badges from "@/components/Badges";
const total = ref(0); // 题目总数
const isLoading = ref<boolean>(true);
const router = useRouter();
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
  const { data, code, message } = await GetQuestions(
    PageInfo.value.current,
    PageInfo.value.pageSize
  );

  if (code === 0 && data) {
    total.value = Number(data.total) ?? 0;
    QuestionInfo.value = Array.isArray(data.records)
      ? data.records.map((item: any) => ({
          ...item,
          id: item.id !== undefined ? String(item.id) : undefined,
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

const go2Solution = (id: string) => {
  router.push({ path: "/solution", query: { id } });
};
onMounted(() => LoadQuestionsInfo());
watchEffect(() => LoadQuestionsInfo());
</script>
