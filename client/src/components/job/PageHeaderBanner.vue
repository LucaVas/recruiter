<script setup lang="ts">
const { title } = defineProps<{
  title: string;
}>();
</script>

<template>
  <div>
    <div class="flex h-16 w-full rounded-md bg-slate-100 p-4">
      <h1 class="self-center text-xl font-bold italic tracking-wide text-blue-500">{{ title }}</h1>
    </div>
  </div>
</template>
