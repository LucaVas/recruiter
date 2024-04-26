<template>
  <div class="card flex flex-col gap-8">
    <div class="flex w-full flex-col gap-6 sm:flex-row">
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm" for="wantedCvs">Bonus Pay per CV</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-wallet" />
          </InputGroupAddon>
          <InputNumber
            id="bonusPayPerCv"
            :min="0"
            v-model="details.bonusPayPerCv"
            required
            :disabled="disabled"
            @input="emit('input', details)"
          />
        </InputGroup>
      </div>
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm" for="wantedCvs">CV Rate Payment Date</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-wallet" />
          </InputGroupAddon>
          <Calendar
            id="cvRatePaymentDate"
            v-model="details.cvRatePaymentDate"
            showIcon
            :minDate="new Date()"
            dateFormat="dd/mm/yy"
            iconDisplay="input"
            inputId="icondisplay"
            required
            :disabled="disabled"
            @date-select="emit('input', details)"
          />
        </InputGroup>
      </div>
    </div>

    <div class="flex w-full flex-col gap-6 sm:flex-row">
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm" for="wantedCvs">Closure Bonus</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-money-bill" />
          </InputGroupAddon>
          <InputNumber
            id="closureBonus"
            :min="0"
            v-model="details.closureBonus"
            required
            :disabled="disabled"
            @input="emit('input', details)"
          />
        </InputGroup>
      </div>
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm" for="wantedCvs">Closure Bonus Payment Date</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-money-bill" />
          </InputGroupAddon>
          <Calendar
            id="closureBonusPaymentDate"
            v-model="details.closureBonusPaymentDate"
            showIcon
            dateFormat="dd/mm/yy"
            :minDate="new Date()"
            iconDisplay="input"
            inputId="icondisplay"
            required
            :disabled="disabled"
            @date-select="emit('input', details)"
          />
        </InputGroup>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import Calendar from 'primevue/calendar';
import { ref } from 'vue';
import type { Job, NewJobRequest } from '@/stores/job/schema';

// props
const { jobDetails, disabled } = defineProps<{
  jobDetails: Job | NewJobRequest;
  disabled: boolean;
}>();

// emits
const emit = defineEmits<{
  (e: 'input', content: typeof details.value): void;
}>();

const details = ref(jobDetails);
</script>
@/stores/job/schema
