<template>
  <div class="grid gap-8 text-center">
    <h1
      class="font-[inherit] m-0 leading-[1.4] tracking-[-0.125px] text-[#12161e] font-semibold text-xl"
    >
      Sign in
    </h1>
    <!-- TODO 表单 -->
    <div>Login</div>
    <div class="grid gap-4 [&:not(:last-child)]:mb-6">
      <p class="m-0 leading-normal text-[#656976] text-sm font-medium">
        Don’t have a SupaAuth account?
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
import { RouterLink } from "vue-router";
</script>
