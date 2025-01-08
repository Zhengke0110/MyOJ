<template>
  <div class="grid gap-8 text-center">
    <h1
      class="font-[inherit] m-0 leading-[1.4] tracking-[-0.125px] text-[#12161e] font-semibold text-xl"
    >
      Sign in
    </h1>
    <!-- 表单 -->
    <div class="w-full">
      <div class="my-4">
        <label class="block text-sm text-left text-gray-500 dark:text-gray-300"
          >Account</label
        >

        <input
          type="text"
          placeholder="please enter your account..."
          class="block mt-2 w-full placeholder-gray-400/70 dark:placeholder-gray-500 rounded-lg border border-gray-200 bg-white px-5 py-2.5 text-gray-700 focus:border-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-40 dark:border-gray-600 dark:bg-gray-900 dark:text-gray-300 dark:focus:border-blue-300"
          v-model="form.userAccount"
        />
      </div>
      <div class="my-4">
        <label
          for="password"
          class="block text-sm text-left text-gray-500 dark:text-gray-300"
          >Password</label
        >

        <div class="relative flex items-center mt-2">
          <button
            class="absolute right-0 focus:outline-none"
            @click="showPasswordMode = !showPasswordMode"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              fill="currentColor"
              class="w-6 h-6 mx-4 text-gray-400 transition-colors duration-300 dark:text-gray-500 hover:text-gray-500 dark:hover:text-gray-400"
            >
              <path d="M12 15a3 3 0 100-6 3 3 0 000 6z" />
              <path
                fill-rule="evenodd"
                d="M1.323 11.447C2.811 6.976 7.028 3.75 12.001 3.75c4.97 0 9.185 3.223 10.675 7.69.12.362.12.752 0 1.113-1.487 4.471-5.705 7.697-10.677 7.697-4.97 0-9.186-3.223-10.675-7.69a1.762 1.762 0 010-1.113zM17.25 12a5.25 5.25 0 11-10.5 0 5.25 5.25 0 0110.5 0z"
                clip-rule="evenodd"
              />
            </svg>
          </button>

          <input
            :type="showPasswordMode ? 'text' : 'password'"
            placeholder="please enter your password"
            class="block w-full py-2.5 text-gray-700 placeholder-gray-400/70 bg-white border border-gray-200 rounded-lg pl-5 pr-11 dark:bg-gray-900 dark:text-gray-300 dark:border-gray-600 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-blue-300 focus:outline-none focus:ring focus:ring-opacity-40"
            v-model="form.userPassword"
          />
        </div>
      </div>
      <button
        class="w-full py-2 my-4 h-10 font-medium tracking-wide text-white capitalize transition-colors duration-300 transform bg-blue-500 rounded-lg hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-200 focus:ring-opacity-80"
        @click="handleSubmit"
      >
        Submit
      </button>
    </div>

    <div class="grid gap-4 [&:not(:last-child)]:mb-6">
      <p class="m-0 leading-normal text-[#656976] text-sm font-medium">
        Don’t have a Doj account?
      </p>

      <RouterLink to="/user/register">
        <button
          class="bg-blue-500 w-full text-white font-semibold py-3 px-6 rounded-lg shadow-sm transition duration-300 ease-in-out hover:bg-blue-600 active:bg-blue-700 scale-95"
        >
          Create new account
        </button>
      </RouterLink>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { RouterLink } from "vue-router";
import { UserLogin } from "@/services/user";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";
import { useUserStore } from "@/store/user";

const router = useRouter();
const userStore = useUserStore();
const form = reactive<{ userAccount: string; userPassword: string }>({
  userAccount: "",
  userPassword: "",
});
const handleSubmit = async () => {
  const { code, data, message } = await UserLogin(form);
  if (code === 0 && data) {
    Message.success("登录成功, 3秒后跳转到首页");
    setTimeout(() => router.push("/"), 3000);
  } else {
    Message.error(`登录失败, ${message}`);
  }

  // 获取当前登录的用户信息
  await userStore.setLoginInfo();
};

// Password Eye Event
const showPasswordMode = ref(false);
</script>
