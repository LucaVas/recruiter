<template>
  <div class="flex h-full w-full flex-col items-center justify-start gap-10 p-10">
    <h3 class="text-lg font-semibold">Sorry, the page you are looking for does not exist.</h3>
    <Button @click="router.push({ name: 'Dashboard' })">Back home</Button>
  </div>
</template>

<script setup lang="ts">
import Button from 'primevue/button';
import { useRouter } from 'vue-router';

const router = useRouter();
</script>
