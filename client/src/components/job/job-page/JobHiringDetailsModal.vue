<script setup lang="ts">
import JobMetadataEntry from '../metadata/JobMetadataEntry.vue';
import { formatDate } from '@/utils/dateUtils';
import Dialog from 'primevue/dialog';
import type { Job } from '@/stores/job/schema';

const { job, visible } = defineProps<{
  job: Job;
  visible: boolean;
}>();

defineEmits<{
  (e: 'close'): void;
}>();
</script>

<template>
  <div class="flex justify-center">
    <Dialog
      :visible="visible"
      @update:visible="$emit('close')"
      closeOnEscape
      modal
      header="Hiring details"
      :style="{ width: '50vw' }"
      :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
    >
      <div class="flex flex-col gap-3">
        <JobMetadataEntry
          :icon="'pi-calendar'"
          :content="`From ${job.experienceRangeMin} to ${job.experienceRangeMax} years of experience`"
        />
        <JobMetadataEntry
          :icon="'pi-clock'"
          :content="`${job.noticePeriodInDays} days of notice`"
        />
        <JobMetadataEntry :icon="'pi-briefcase'" :content="`${job.salaryBudget} ${job.currency}`" />
        <JobMetadataEntry
          :icon="'pi-money-bill'"
          :content="`${job.bonusPayPerCv} INR per CV paid on ${formatDate(job.cvRatePaymentDate)}`"
        />
        <JobMetadataEntry
          v-if="job.closureBonus.toLowerCase() !== 'not applicable' && job.closureBonus !== ''"
          :icon="'pi-wallet'"
          :content="`${job.closureBonus} INR closure bonus paid on ${formatDate(job.closureBonusPaymentDate)}`"
        />
      </div>
      <div class="mt-5 flex justify-end gap-2">
        <Button
          type="button"
          label="Close"
          rounded
          severity="secondary"
          @click="$emit('close')"
        ></Button>
      </div>
    </Dialog>
  </div>
</template>
@/stores/job/schema
