<template>
  <div class="flex w-full flex-col gap-4">
    <label
      class="min-w-fit cursor-pointer rounded-md border border-slate-300 bg-white p-3 text-center text-sm font-semibold text-[#3b81f6]"
    >
      <input type="file" accept="application/pdf" multiple @change="onUpload($event)" />
      <i class="pi pi-upload mr-2"></i>
      Upload
    </label>
    <div v-if="files" class="flex max-h-32 flex-col gap-3 overflow-y-auto">
      <div v-for="file in files" :key="file.name" class="flex w-full justify-between">
        <span class="file-name max-w-[90%] overflow-x-auto whitespace-nowrap">{{ file.name }}</span>
        <Button
          class="min-w-fit"
          icon="pi pi-trash"
          unstyled
          @click="files.splice(files.indexOf(file), 1)"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const files = ref<File[]>([]);
const onUpload = async (event: Event) => {
  if (!(event.target instanceof HTMLInputElement) || !event.target.files) return;
  const targetFiles = event.target.files;
  for (let i = 0; i < targetFiles.length; ++i) {
    const file = targetFiles[i];
    files.value.push(file);
  }
  emits('addFiles', files.value);
};

const emits = defineEmits<{
  (e: 'addFiles', files: File[]): void;
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
