<script setup lang="ts">
import JobMetadataEntry from '../metadata/JobMetadataEntry.vue';
import { formatDate } from '@/utils/dateUtils';
import type { Currency } from '@/stores/job/types';
import Dialog from 'primevue/dialog';

const {
  visible,
  experienceRangeMin,
  experienceRangeMax,
  noticePeriodInDays,
  salaryBudget,
  currency,
  bonusPayPerCv,
  closureBonus,
  closureBonusPaymentDate,
  cvRatePaymentDate,
} = defineProps<{
  visible: boolean;
  experienceRangeMin: number;
  experienceRangeMax: number;
  noticePeriodInDays: number;
  salaryBudget: number;
  currency: Currency;
  bonusPayPerCv: number;
  closureBonus: string;
  closureBonusPaymentDate: string;
  cvRatePaymentDate: string;
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
          :content="`From ${experienceRangeMin} to ${experienceRangeMax} years of experience`"
        />
        <JobMetadataEntry :icon="'pi-clock'" :content="`${noticePeriodInDays} days of notice`" />
        <JobMetadataEntry :icon="'pi-briefcase'" :content="`${salaryBudget} ${currency}`" />
        <JobMetadataEntry
          :icon="'pi-money-bill'"
          :content="`${bonusPayPerCv} INR per CV paid on ${formatDate(cvRatePaymentDate)}`"
        />
        <JobMetadataEntry
          :icon="'pi-wallet'"
          :content="`${closureBonus} INR closure bonus paid on ${formatDate(closureBonusPaymentDate)}`"
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
