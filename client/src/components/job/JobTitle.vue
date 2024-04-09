<script setup lang="ts">

const { title } = defineProps<{
  title: string;
}>();
</script>

<template>
  <div class="w-full">
    <h1
      class="text-2xl font-extrabold leading-none tracking-tight text-gray-900 md:text-3xl lg:text-4xl"
    >
      {{ title }}
    </h1>
  </div>
</template>
