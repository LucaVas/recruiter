<template>
  <Dialog
    :visible="visible"
    modal
    header="Candidacy documents"
    @update:visible="$emit('close')"
    :style="{ width: '50rem' }"
    :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
  >
    <div class="flex flex-col gap-4">
      <div v-for="file in files" :key="file.id" class="flex w-full items-center">
        <i class="pi pi-file mr-2" />
        <div class="flex w-full justify-between">
          <span>{{ file.name }}</span>
          <span>{{ formatDateTime(file.createdDTime) }}</span>
        </div>
      </div>
    </div>
  </Dialog>
</template>

<script setup lang="ts">
import { type CandidacyFile } from '@/stores/candidacy/schema';
import { formatDateTime } from '@/utils/dateUtils';

const { visible, files } = defineProps<{
  visible: boolean;
  files: CandidacyFile[];
}>();

defineEmits<{
  (e: 'close'): void;
}>();
</script>
