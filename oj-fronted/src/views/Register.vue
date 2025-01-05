<template>
  <div class="grid gap-8 text-center">
    <h1
      class="font-[inherit] m-0 leading-[1.4] tracking-[-0.125px] text-[#12161e] font-semibold text-xl"
    >
      Create an account
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
      <a-form-item field="checkPassword" label="CheckPassword">
        <a-input-password
          v-model="form.checkPassword"
          placeholder="please enter your password here again"
        />
      </a-form-item>

      <a-form-item>
        <a-button type="outline" long @click="handleSubmit">Submit</a-button>
      </a-form-item>
    </a-form>

    <div class="grid gap-4 [&:not(:last-child)]:mb-6">
      <RouterLink to="/user/login">
        <button
          class="bg-blue-500 w-full text-white font-semibold py-3 px-6 rounded-lg shadow-sm transition duration-300 ease-in-out hover:bg-blue-600 active:bg-blue-700 scale-95"
        >
          Sign up
        </button>
      </RouterLink>
    </div>
  </div>
</template>

<script setup lang="ts">
import { RouterLink } from "vue-router";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";
import { reactive } from "vue";
import { UserRegister } from "@/services/user";

const router = useRouter();

const form = reactive<{
  userAccount: string;
  userPassword: string;
  checkPassword: string;
}>({
  userAccount: "",
  userPassword: "",
  checkPassword: "",
});

const handleSubmit = async () => {
  const { code, data, message } = await UserRegister(form);
  if (code === 0 && data) {
    Message.success("注册成功, 请登录");
    setTimeout(() => router.push("/user/login"), 1000);
  } else {
    Message.error(`注册失败, ${message}`);
  }
};
</script>
