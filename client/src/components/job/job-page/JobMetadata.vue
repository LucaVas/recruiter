<script setup lang="ts">
import type { Job } from '@/stores/job/schema';
import JobMetadataEntry from '../metadata/JobMetadataEntry.vue';
import { capitalize } from '@/utils/stringUtils';
import { formatDate } from '@/utils/dateUtils';
import { formatStatus } from '../shared/utils';

const { job } = defineProps<{
  job: Job;
}>();
</script>

<template>
  <div class="flex w-full flex-col gap-2">
    <JobMetadataEntry :icon="'pi-building'" :content="job.client.name" />
    <JobMetadataEntry
      :icon="'pi-briefcase'"
      :content="`${capitalize(job.contractType)} contract`"
    />
    <JobMetadataEntry :icon="'pi-file'" :content="`${job.wantedCvs} CVs wanted`" />
    <JobMetadataEntry :icon="'pi-users'" :content="`${job.numberOfCandidates ?? 0} candidates`" />
    <JobMetadataEntry :icon="'pi-calendar'" :content="formatDate(job.createdDTime)" />
    <JobMetadataEntry :icon="'pi-info-circle'" :content="formatStatus(job.status) ?? ''" />
  </div>
</template>
../utils
