<template>
  <Dialog
    :visible="visible"
    modal
    header="Candidacy documents"
    @update:visible="$emit('close')"
    :style="{ width: '50rem' }"
    :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
  >
    <div v-if="loading" class="flex w-full items-center justify-center">
      <ProgressSpinner />
    </div>
    <div v-else class="max-h-42 flex flex-col gap-4 overflow-y-auto">
      <div v-if="files.length === 0">No files available.</div>
      <div v-else>
        <div v-for="file in files" :key="file.id" class="flex w-full items-center border-b py-4">
          <i class="pi pi-file mr-2" />
          <div class="flex w-full justify-between">
            <span>{{ file.name }}</span>
            <div class="flex items-center gap-2">
              <Button
                icon="pi pi-download"
                unstyled
                @click="$emit('download', file)"
                :loading="downloading"
              />
              <Button icon="pi pi-trash" unstyled @click="$emit('delete', file.id)" />
              <i
                class="pi pi-info-circle mr-2"
                v-tooltip.left="'Uploaded on ' + formatDateTime(file.createdAt)"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
    <template #footer>
      <Button label="Upload more" outlined @click="$emit('upload')" />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import Dialog from 'primevue/dialog';
import ProgressSpinner from 'primevue/progressspinner';
import Button from 'primevue/button';
import { type CandidacyFile } from '@/types/candidacyTypes';
import { formatDateTime } from '@/utils/dateUtils';

const { visible, files, loading, downloading } = defineProps<{
  visible: boolean;
  files: CandidacyFile[];
  loading: boolean;
  downloading: boolean;
}>();

defineEmits<{
  (e: 'close'): void;
  (e: 'download', file: CandidacyFile): void;
  (e: 'delete', fileId: number): void;
  (e: 'upload'): void;
}>();
</script>
