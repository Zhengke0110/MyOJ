<template>
  <div>QuestionDetails</div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
const router = useRouter();
const route = useRoute();
onMounted(() => {
  console.log(route.query);
});
</script>

<style scoped></style>
