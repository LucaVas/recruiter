<script setup lang="ts">
import type { ContractTypeName, JobStatus } from '@/stores/job/types';
import JobMetadataEntry from './metadata/JobMetadataEntry.vue';
import { capitalize } from '@/utils/stringUtils';
import { formatDate } from '@/utils/dateUtils';

const { client, wantedCvs, candidates, createdOn, status } = defineProps<{
  client: string;
  contractType: ContractTypeName;
  wantedCvs: number;
  candidates: number;
  createdOn: string;
  status: JobStatus;
}>();
</script>

<template>
  <div class="flex w-full flex-col gap-2">
    <JobMetadataEntry :icon="'pi-building'" :content="client" />
    <JobMetadataEntry :icon="'pi-briefcase'" :content="`${capitalize(contractType)} contract`" />
    <JobMetadataEntry :icon="'pi-file'" :content="`${wantedCvs} CVs wanted`" />
    <JobMetadataEntry :icon="'pi-users'" :content="`${candidates} candidates`" />
    <JobMetadataEntry :icon="'pi-calendar'" :content="formatDate(createdOn)" />
    <JobMetadataEntry :icon="'pi-info-circle'" :content="status" />
  </div>
</template>
