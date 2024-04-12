<script setup lang="ts">
import { getSeverity, getStatusIcon, formatStatus } from '@/components/job/shared/utils';
import type { JobStatus } from '@/stores/job/types';

const { status, name, client } = defineProps<{
  status: JobStatus;
  name: string;
  client: string;
}>();

defineEmits<{
  (e: 'openModal'): void;
}>();
</script>

<template>
  <div class="flex w-full items-center justify-between">
    <div class="flex flex-col items-start gap-4">
      <p class="text-xl font-semibold">{{ name }} ({{ client }})</p>
      <Tag
        :icon="getStatusIcon(status)"
        :severity="getSeverity(status)"
        class="h-10 min-w-fit px-4"
        :value="formatStatus(status)"
      />
    </div>
    <Button
      rounded
      type="button"
      size="small"
      class="min-w-fit"
      label="View Details"
      outlined
      @click="$emit('openModal')"
    />
  </div>
  <Divider />
</template>
@/components/job/update-job/utils
