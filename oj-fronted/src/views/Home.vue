<template>
  <div
    class="container w-full mx-auto flex flex-col items-center bg-write animated animated-duration-500 animated-fade-in"
  >
    <div
      class="lg:flex-grow mt-5 md:mt-0 md:w-1.5/2 lg:pr-24 md:pr-16 flex flex-col md:items-start md:text-left mb-16 md:mb-0 items-center text-center"
    >
      <h1
        class="text-2xl font-extrabold leading-9 tracking-tight mb-3 text-gray-900 dark:text-gray-100 sm:text-4xl sm:leading-10 md:text-5xl md:leading-normal"
      >
        <span class="sm:text-5xl md:text-6xl">D</span> oj
      </h1>
      <p
        class="mb-8 md:pl-0 pl-2 pr-2 font-bold leading-relaxed dark:text-gray-300"
      >
        A code sandbox project written by the Java language.
      </p>
    </div>
    <div class="lg:max-w-lg lg:w-full mb-5 md:mb-0 md:w-1/2 w-3/5">
      <img
        class="object-cover object-center rounded w-full h-full"
        alt="hero"
        :src="HotPath"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { HotPath } from "@/config";
</script>

<style scoped></style>
