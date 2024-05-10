<template>
  <div class="flex w-full flex-col md:items-center md:flex-row gap-4">
    <label
      class="rounded-md border border-slate-300 bg-white text-center p-3 text-sm font-semibold text-[#3b81f6] min-w-fit cursor-pointer"
    >
      <input type="file" accept="application/pdf" @change="onUpload($event)" />
      <i class="pi pi-upload mr-2"></i>
      Upload
    </label>
    <div v-if="file" class="w-full flex justify-between">
      <span class="file-name max-w-[90%] overflow-x-auto whitespace-nowrap">{{ file.name }}</span>
      <Button class="min-w-fit" icon="pi pi-trash" unstyled @click="file = undefined" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const file = ref<File>();
const onUpload = async (event: Event) => {
  if (!(event.target instanceof HTMLInputElement) || !event.target.files) return;
  file.value = event.target.files[0];
  emits('addFile', file.value);
};

const emits = defineEmits<{
  (e: 'addFile', file: File): void;
}>();
</script>

<style scoped>
input[type='file'] {
  display: none;
}

.custom-file-upload {
  border: 1px solid #ccc;
  display: inline-block;
  padding: 6px 12px;
  cursor: pointer;
}
</style>
