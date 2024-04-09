<script setup lang="ts">
const { description } = defineProps<{
  description: string;
}>();
</script>

<template>
  <div class="flex w-full flex-col gap-3">
    <h3 class="text-lg font-medium">Job Description</h3>
    <p>
      {{ description }}
    </p>
  </div>
</template>
