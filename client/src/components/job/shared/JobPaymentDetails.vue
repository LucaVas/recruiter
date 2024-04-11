<template>
  <div class="card flex flex-col gap-8">
    <div class="flex w-full flex-row gap-6">
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm" for="wantedCvs">Bonus Pay per CV</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-wallet" />
          </InputGroupAddon>
          <InputText
            id="bonusPayPerCv"
            type="number"
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

    <div class="flex w-full flex-row gap-6">
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm" for="wantedCvs">Closure Bonus</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-money-bill" />
          </InputGroupAddon>
          <InputText
            id="closureBonus"
            type="text"
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
import InputText from 'primevue/inputtext';
import Calendar from 'primevue/calendar';
import { ref, onMounted } from 'vue';
import type { JobDto, NewJob } from '@/stores/job/types';

// variables
const details = ref({
  bonusPayPerCv: '',
  closureBonus: '',
  cvRatePaymentDate: '',
  closureBonusPaymentDate: '',
});

// props
const { jobDetails, disabled } = defineProps<{
  jobDetails: JobDto | NewJob;
  disabled: boolean;
}>();

// emits
const emit = defineEmits<{
  (e: 'input', content: typeof details.value): void;
}>();

// init
onMounted(() => {
  details.value = jobDetails;
});
</script>
