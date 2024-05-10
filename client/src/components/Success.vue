<template>
  <div class="flex h-full w-full flex-col items-center justify-start gap-10 p-10">
    <Image src="/src/assets/images/candidacies/done.svg" alt="Image" class="w-1/3" />
    <h3 class="text-lg font-semibold">{{ message }}</h3>
  </div>
</template>

<script setup lang="ts">
import Image from 'primevue/image';
const { message } = defineProps<{
  message: string;
}>();
defineEmits<{
  (e: 'go'): void;
}>();
</script>
