<template>{{ message }}</template>

<script setup lang="ts">
const { message } = defineProps<{
  message: string;
}>();
</script>
