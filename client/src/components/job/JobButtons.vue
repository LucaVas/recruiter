<script setup lang="ts">
import Button from 'primevue/button';
import type { JobStatus } from '@/stores/job/types';
import { useRouter } from 'vue-router';

const router = useRouter();
const { id, status } = defineProps<{
  id: number;
  status: JobStatus;
}>();
defineEmits<{
  (e: 'openModal'): void
}>()
</script>

<template>
  <div class="flex w-full items-center gap-2">
    <Button
      rounded
      type="button"
      size="small"
      label="Submit Candidacy"
      v-if="status !== 'ARCHIVED'"
      @click="router.push({ name: 'NewCandidacy', params: { id: id } })"
    />
    <Button
      rounded
      type="button"
      size="small"
      label="View Details"
      outlined
      v-if="status !== 'ARCHIVED'"
      @click="$emit('openModal')"
    />
  </div>
  <Divider />
</template>
