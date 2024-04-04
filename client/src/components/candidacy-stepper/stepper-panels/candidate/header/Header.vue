<script setup lang="ts">
const { index, currentIndex } = defineProps<{
  index: number;
  currentIndex: number;
}>();
</script>

<template>
  <span
    :class="[
      'border-round w-3rem h-3rem align-items-center justify-content-center inline-flex border-2',
      {
        'bg-primary border-primary': index <= currentIndex,
        'surface-border': index > currentIndex,
      },
    ]"
  >
    <i class="pi pi-user" />
  </span>
</template>
