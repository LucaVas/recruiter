<template>
  <div class="flex justify-center">
    <input type="file" accept="application/pdf" @change="onUpload($event)" />
  </div>
</template>

<script setup lang="ts">
const onUpload = async (event: Event) => {
  if (!(event.target instanceof HTMLInputElement) || !event.target.files) return;
  const file = event.target.files[0];
  emits('addFile', file);
};

const emits = defineEmits<{
  (e: 'addFile', file: File): void;
}>();
</script>
