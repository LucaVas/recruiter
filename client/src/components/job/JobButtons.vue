<script setup lang="ts">
import Button from 'primevue/button';
import type { JobStatus } from '@/stores/job/types';

const { status } = defineProps<{
  status: JobStatus;
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
      @click="null"
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
</template>
