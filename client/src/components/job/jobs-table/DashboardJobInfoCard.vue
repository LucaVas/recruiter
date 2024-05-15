<template>
  <div class="flex items-start gap-5">
    <div class="h-30 flex flex-col items-start gap-4">
      <div class="flex flex-col gap-3">
        <JobMetadataEntry
          :icon="'pi-calendar'"
          :content="`From ${job.experienceRangeMin} to ${job.experienceRangeMax} years of experience`"
        />
        <JobMetadataEntry
          :icon="'pi-clock'"
          :content="`${job.noticePeriodInDays} days of notice`"
        />
        <div class="flex w-full">
          <JobMetadataEntry
            :icon="'pi-briefcase'"
            :content="`${job.salaryBudget} ${job.currency}`"
          />
          <JobMetadataEntry :icon="'pi-money-bill'" :content="`${job.bonusPayPerCv} INR per CV`" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Job } from '@/stores/job/schema';
import JobMetadataEntry from '../metadata/JobMetadataEntry.vue';

const { job } = defineProps<{
  job: Job;
}>();
</script>
