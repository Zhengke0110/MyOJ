<template>
  <div class="grid gap-8 text-center">
    <h1
      class="font-[inherit] m-0 leading-[1.4] tracking-[-0.125px] text-[#12161e] font-semibold text-xl"
    >
      Sign in
    </h1>
    <!-- TODO 表单 -->

    <a-form :model="form" class="w-full" layout="vertical">
      <a-form-item field="name" label="Account">
        <a-input
          v-model="form.userAccount"
          placeholder="please enter your account..."
        />
      </a-form-item>

      <a-form-item field="password" label="Password">
        <a-input-password
          v-model="form.userPassword"
          placeholder="please enter your password"
        />
      </a-form-item>

      <a-form-item>
        <a-button type="outline" long @click="handleSubmit">Submit</a-button>
      </a-form-item>
    </a-form>

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
import { reactive } from "vue";
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
</script>
