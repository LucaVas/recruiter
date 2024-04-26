<script setup lang="ts">
import JobMetadataEntry from '@/components/job/metadata/JobMetadataEntry.vue';
import { formatDate } from '@/utils/dateUtils';
import type { Job } from '@/stores/job/types';
import Dialog from 'primevue/dialog';
import { capitalize } from '@/utils/stringUtils';
import { ref } from 'vue';

const { visible, job } = defineProps<{
  visible: boolean;
  job: Job;
}>();

defineEmits<{
  (e: 'close'): void;
}>();

const details = ref(job);
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
      v-if="details"
    >
      <div class="flex flex-col gap-3">
        <JobMetadataEntry
          :icon="'pi-briefcase'"
          :content="`${capitalize(details.contractType.contractTypeName)} contract`"
        />
        <JobMetadataEntry :icon="'pi-file'" :content="`${details.wantedCvs} CVs wanted`" />
        <JobMetadataEntry
          :icon="'pi-users'"
          :content="`${details.numberOfCandidates} candidates`"
        />
        <JobMetadataEntry
          :icon="'pi-calendar'"
          :content="`From ${details.experienceRangeMin} to ${details.experienceRangeMax} years of experience`"
        />
        <JobMetadataEntry
          :icon="'pi-clock'"
          :content="`${details.noticePeriodInDays} days of notice`"
        />
        <JobMetadataEntry
          :icon="'pi-briefcase'"
          :content="`${details.salaryBudget} ${details.currency}`"
        />
        <JobMetadataEntry
          :icon="'pi-money-bill'"
          :content="`${details.bonusPayPerCv} INR per CV paid on ${formatDate(details.cvRatePaymentDate)}`"
        />
        <JobMetadataEntry
          :icon="'pi-wallet'"
          :content="`${details.closureBonus} INR closure bonus paid on ${formatDate(details.closureBonusPaymentDate)}`"
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
