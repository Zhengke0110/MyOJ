<!-- 浏览题目 - 非管理员可见 -->
<template>
  <div class="w-full">
    <table class="min-w-full divide-y divide-gray-300">
      <thead>
        <tr>
          <th
            v-for="item in QuestionTableColumns"
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
            class="whitespace-nowrap px-3 py-4 text-sm font-bold text-gray-900"
          >
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
              class="inline-flex items-center gap-x-1.5 rounded-md bg-indigo-600 px-2.5 py-1.5 mx-1 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            >
              <i
                class="i-tabler:brand-google-filled size-5"
                @click="go2Solution(item.id as string)"
              />
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
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { type QuestionInterface, QuestionTableColumns } from "@/config";
import { GetQuestions } from "@/services/question";
import { Message } from "@arco-design/web-vue";
import { onMounted, ref, watchEffect } from "vue";
import Pagination from "@/components/Pagination";
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

const go2Solution = (id: string) => {
  router.push({ path: "/solution", query: { id } });
};
onMounted(() => LoadQuestionsInfo());
watchEffect(() => LoadQuestionsInfo());
</script>
