c
<script setup lang="ts">
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputText from 'primevue/inputtext';
import { ref } from 'vue';

const details = ref({
  bonusPayPerCv: '',
  closureBonus: '',
  cvRatePaymentDate: '',
  closureBonusPaymentDate: '',
});

const emit = defineEmits<{
  (e: 'input', content: typeof details.value): void;
}>();
</script>

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
          <InputText
            id="cvRatePaymentDate"
            type="date"
            v-model="details.cvRatePaymentDate"
            required
            @input="emit('input', details)"
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
            v-model="details.closureBonus"
            required
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
          <InputText
            id="closureBonus"
            type="date"
            v-model="details.closureBonusPaymentDate"
            required
            @input="emit('input', details)"
          />
        </InputGroup>
      </div>
    </div>
  </div>
</template>
