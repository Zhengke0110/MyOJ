<template>
  <div class="w-full animated animated-duration-500 animated-fade-in">
    <ul role="list" class="divide-y divide-gray-100">
      <li
        v-for="item in SubmitList"
        :key="item.id"
        class="relative flex justify-between py-5"
      >
        <div class="flex gap-x-4 pr-6 sm:w-1/5 sm:flex-none">
          <div class="hidden sm:block">
            <p class="text-base/6 font-bold block text-gray-900">
              {{ item.question.title }}
            </p>

            <div class="mt-1 flex items-center gap-x-1.5">
              <div
                class="flex-none rounded-full p-1"
                :class="
                  item.judgeInfo.message === Accepted
                    ? 'bg-emerald-500/20'
                    : 'bg-red-500/20'
                "
              >
                <div
                  class="size-1.5 rounded-full"
                  :class="
                    item.judgeInfo.message === Accepted
                      ? 'bg-emerald-500'
                      : 'bg-red-500'
                  "
                />
              </div>

              <p
                class="text-xs/5 font-extrabold mx-2"
                :class="
                  item.judgeInfo.message === Accepted
                    ? 'text-emerald-500'
                    : 'text-red-500'
                "
              >
                {{
                  item.judgeInfo.message != ""
                    ? item.judgeInfo.message
                    : "未知错误"
                }}
              </p>
            </div>
          </div>
        </div>

        <div
          class="flex items-center justify-between gap-x-4 sm:w-1/3 sm:flex-none"
        >
          <img
            class="size-12 flex-none rounded-full bg-gray-50"
            :src="
              item.submitter.userAvatar != ''
                ? item.submitter.userAvatar
                : DefaultUserAvatar
            "
          />
          <div class="min-w-0 flex-auto">
            <p class="text-sm/6 font-semibold text-gray-900">
              <span class="absolute inset-x-0 -top-px bottom-0" />

              {{ item.submitter.userName }}
            </p>
          </div>
        </div>
        <div
          class="flex items-center justify-between gap-x-4 sm:w-1/3 sm:flex-none"
        >
          <div class="min-w-0 flex-auto">
            <div class="flex items-center justifiy-between">
              <p
                class="text-xs/5 text-gray-500 font-semibold mr-2 flex items-center"
              >
                <i class="i-tabler:device-imac-code size-3.5 mr-1"> </i>
                <span>{{ item.language }}</span>
              </p>
              <p
                class="text-xs/5 text-gray-500 font-semibold mx-2 flex items-center"
              >
                <i class="i-tabler:alarm size-4 mr-1"></i>
                <span>执行用时: {{ item.judgeInfo.time }} ms</span>
              </p>
            </div>
            <p class="mt-1 flex text-xs/5 text-gray-500 font-semibold">
              提交时间:<span class="ml-2"> {{ item.createTime }}</span>
            </p>
          </div>

          <i class="i-tabler:dots-vertical size-5" />
        </div>
      </li>
    </ul>
    <Pagination
      :total="Total"
      :current="PageInfo.current"
      :page-size="PageInfo.pageSize"
      @previous="PreviousHandle"
      @next="NextHandle"
      @select-page="SelectPageHandle"
    />
  </div>
</template>

<script lang="ts" setup>
import { ref, watchEffect } from "vue";
import { DefaultUserAvatar, type SubmitRecordInterface } from "@/config";
import { GetQuestionSubmitList } from "@/services/question";
import { Message } from "@arco-design/web-vue";
import dayjs from "dayjs";
import Pagination from "@/components/Pagination";

const Accepted = "Accepted";

// 分页请求数据
const PageInfo = ref<{ current: number; pageSize: number }>({
  current: 1,
  pageSize: 20,
});

const PreviousHandle = (current: number) =>
  (PageInfo.value = { ...PageInfo.value, current: current });

const NextHandle = (current: number) => {
  PageInfo.value = { ...PageInfo.value, current: current };
};
const SelectPageHandle = (current: number) =>
  (PageInfo.value = { ...PageInfo.value, current: current });

const SubmitList = ref<SubmitRecordInterface[]>([]);
const Total = ref<number>(0);
const isLoading = ref<boolean>(true); // TODO 未使用

const LoadQuestionSubmitList = async () => {
  isLoading.value = true;
  try {
    const { data, code } = await GetQuestionSubmitList(
      PageInfo.value.current,
      PageInfo.value.pageSize
    );

    if (code === 0 && data) {
      const { records, total } = data;
      Total.value = Number(total) ?? 0;
      SubmitList.value = Array.isArray(records)
        ? records.map((item) => ({
            id: item.id ? String(item.id) : "",
            language: item.language ?? "",
            question: {
              id: item.questionId ? String(item.questionId) : "",
              title: item.questionVO?.title ?? "",
              tags: item.questionVO?.tags ?? [],
            },
            judgeInfo: {
              memory: item.judgeInfo?.memory ?? 0,
              time: item.judgeInfo?.time ?? 0,
              message: item.judgeInfo?.message ?? "",
            },
            submitter: {
              userName: item.userVO?.userName ?? "",
              userAvatar: item.userVO?.userAvatar ?? DefaultUserAvatar,
            },
            createTime:
              dayjs(item.createTime).format("YYYY-MM-DD HH:mm:ss") ?? "",
          }))
        : [];
    } else Message.error("获取提交列表失败");
  } catch (error) {
  } finally {
    isLoading.value = false;
  }
};

watchEffect(() => LoadQuestionSubmitList());
</script>
