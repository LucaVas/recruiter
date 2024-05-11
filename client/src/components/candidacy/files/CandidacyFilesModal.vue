<template>
  <Dialog
    :visible="visible"
    modal
    header="Candidacy Documents"
    @update:visible="$emit('close')"
    :style="{ width: '50rem' }"
    :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
  >
    <div class="flex flex-col gap-4">
      <div v-for="file in files" :key="file.id">
        {{ file }}
      </div>
    </div>
  </Dialog>
</template>

<script setup lang="ts">
import { type CandidacyFile } from '@/stores/candidacy/schema';

const { visible, files } = defineProps<{
  visible: boolean;
  files: CandidacyFile[];
}>();

defineEmits<{
  (e: 'close'): void;
}>();
</script>
