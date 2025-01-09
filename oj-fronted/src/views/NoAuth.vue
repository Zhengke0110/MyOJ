<template>
  <div>
    noauth

    <Successfully :info="'hhhhhhh noauth'" />
    <Error />
    <Warning />
    <Info />
  </div>
</template>

<script setup lang="ts">
import { Successfully, Error, Warning, Info } from "@/components/GlobalAlters";
</script>
