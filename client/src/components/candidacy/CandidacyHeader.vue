<script setup lang="ts">
import { getJobSeverity, getJobStatusIcon, formatJobStatus } from '@/components/job/utils';
import type { CandidacyDto, NewCandidacy } from '@/stores/candidacy/schema';
import type { Job } from '@/stores/job/schema';
import { getCandidacyStatus, getCandidacyStatusSeverity, getCandidacyStatusIcon } from '../tables/candidacies/utils';

const props = defineProps<{
  candidacy: CandidacyDto | NewCandidacy;
  job?: Job;
}>();

const isCandidacyDto = (obj: any): obj is CandidacyDto => 'status' in obj;

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
      <div class="flex gap-3">
        <Tag
          :icon="getJobStatusIcon(props.job ? props.job.status : props.candidacy.job.status)"
          :severity="getJobSeverity(props.job ? props.job.status : props.candidacy.job.status)"
          class="h-10 min-w-fit px-4"
          :value="`Job ${formatJobStatus(props.job ? props.job.status : props.candidacy.job.status)}`"
        />
        <Tag
          v-if="isCandidacyDto(props.candidacy) && props.candidacy.status"
          class="h-10 min-w-fit px-4"
          :icon="getCandidacyStatusIcon(props.candidacy.status)"
          :severity="getCandidacyStatusSeverity(props.candidacy.status)"
          :value="`Candidacy ${getCandidacyStatus(props.candidacy.status)}`"
        ></Tag>
      </div>
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
