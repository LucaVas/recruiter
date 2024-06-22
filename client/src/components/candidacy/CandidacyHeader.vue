<script setup lang="ts">
import { getSeverity, getStatusIcon, formatStatus } from '@/components/job/utils';
import type { Candidacy, NewCandidacy } from '@/stores/candidacy/schema';
import type { Job } from '@/stores/job/schema';

const props = defineProps<{
  candidacy: Candidacy | NewCandidacy;
  job?: Job;
}>();

defineEmits<{
  (e: 'openModal'): void;
}>();
</script>

<template>
  <div class="flex w-full items-center justify-between">
    <div class="flex flex-col items-start gap-4">
      <p class="text-xl font-semibold">
        {{ props.job ? props.job.name : props.candidacy.job.name }} ({{
          props.job ? props.job.client.name : props.candidacy.job.client.name
        }})
      </p>
      <Tag
        :icon="getStatusIcon(props.job ? props.job.status : props.candidacy.job.status)"
        :severity="getSeverity(props.job ? props.job.status : props.candidacy.job.status)"
        class="h-10 min-w-fit px-4"
        :value="formatStatus(props.job ? props.job.status : props.candidacy.job.status)"
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
