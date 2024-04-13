<script setup lang="ts">
import Button from 'primevue/button';
import type { JobStatus } from '@/stores/job/types';
import { useRouter } from 'vue-router';
import { isAdmin } from '@/stores/auth';

const router = useRouter();
const { id, status } = defineProps<{
  id: number;
  status: JobStatus;
}>();
defineEmits<{
  (e: 'openModal'): void;
  (e: 'deleteJob'): void
}>();
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
    <Button
      rounded
      type="button"
      size="small"
      severity="warning"
      label="Edit"
      outlined
      v-if="status !== 'ARCHIVED' && isAdmin"
      @click="router.push({ name: 'UpdateJob', params: { id: id } })"
    />
    <Button
      rounded
      type="button"
      size="small"
      severity="danger"
      label="Delete"
      outlined
      v-if="status !== 'ARCHIVED' && isAdmin"
      @click="$emit('deleteJob')"
    />
  </div>
  <Divider />
</template>
