<template>
  <Dialog
    :visible="visible"
    modal
    header="Upload documents"
    @update:visible="$emit('close')"
    :style="{ width: '50rem' }"
    :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
  >
    <div class="flex flex-col gap-4">
      <FilesUploader @addFiles="(files: File[]) => (newFiles = files)" />
    </div>
    <template #footer>
      <Button :loading="uploading" label="Upload" @click="$emit('upload', newFiles)" />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import Dialog from 'primevue/dialog';
import FilesUploader from '@/components/uploader/FilesUploader.vue';
import { ref } from 'vue';

const newFiles = ref<File[]>([]);

const { visible, uploading } = defineProps<{
  visible: boolean;
  uploading: boolean;
}>();

defineEmits<{
  (e: 'upload', files: File[]): void;
  (e: 'close'): void;
}>();
</script>
