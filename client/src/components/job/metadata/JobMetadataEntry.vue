<script setup lang="ts">
const { icon, content } = defineProps<{
  icon: string;
  content: string;
}>();
</script>

<template>
  <div class="flex w-full items-center gap-4">
    <i :class="`pi ${icon}`" />
    <p class="min-w-fit">{{ content }}</p>
  </div>
</template>
