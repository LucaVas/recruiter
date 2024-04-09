<script setup lang="ts">
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputNumber from 'primevue/inputnumber';
import Dropdown from 'primevue/dropdown';
import Textarea from 'primevue/textarea';
import type { JobDto, Currency } from '../../stores/job/types';
import { ref, onMounted } from 'vue';

const { jobDetails, isArchived } = defineProps<{
  jobDetails: JobDto;
  isArchived: boolean;
}>();
const updatedJobDetails = ref<Partial<JobDto>>({
  wantedCvs: 0,
  noticePeriodInDays: 0,
  experienceRangeMin: 0,
  experienceRangeMax: 45,
  salaryBudget: 0,
  currency: 'INR',
  description: '',
});
const currencies = ref<Currency[]>(['INR']);

onMounted(() => {
  updatedJobDetails.value = jobDetails;
});
</script>

<template>
  <div class="flex flex-col gap-8">
    <div class="flex w-full flex-row gap-6">
      <div class="flex w-full flex-col gap-2">
        <label for="wantedCvs">Wanted CVs</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-file" />
          </InputGroupAddon>
          <InputNumber
            id="wantedCvs"
            v-model="updatedJobDetails.wantedCvs"
            required
            :disabled="isArchived"
          />
        </InputGroup>
      </div>
      <div class="flex w-full flex-col gap-2">
        <label for="noticePeriodInDays">Notice Period</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-calendar-times" />
          </InputGroupAddon>
          <InputNumber
            id="noticePeriodInDays"
            v-model="updatedJobDetails.noticePeriodInDays"
            required
            :disabled="isArchived"
          />
          <InputGroupAddon class="min-w-fit">days</InputGroupAddon>
        </InputGroup>
      </div>
    </div>

    <div class="flex w-full flex-col gap-2">
      <label for="experienceRange">Experience range</label>
      <div class="flex flex-row gap-6">
        <div class="w-full">
          <InputGroup>
            <InputGroupAddon class="min-w-fit"> From </InputGroupAddon>
            <InputNumber
              id="experienceRangeMin"
              v-model="updatedJobDetails.experienceRangeMin"
              :disabled="isArchived"
              required
            />
            <InputGroupAddon class="min-w-fit"> Years </InputGroupAddon>
          </InputGroup>
        </div>
        <div class="w-full">
          <InputGroup>
            <InputGroupAddon class="min-w-fit"> To </InputGroupAddon>
            <InputNumber
              id="experienceRangeMax"
              v-model="updatedJobDetails.experienceRangeMax"
              :disabled="isArchived"
              required
            />
            <InputGroupAddon class="min-w-fit"> Years </InputGroupAddon>
          </InputGroup>
        </div>
      </div>
    </div>

    <div class="flex w-full flex-col gap-2">
      <label for="experienceRange">Salary Budget</label>
      <InputGroup class="w-full">
        <InputGroupAddon>
          <i class="pi pi-money-bill" />
        </InputGroupAddon>
        <InputNumber
          class="w-full"
          id="salaryBudget"
          v-model="updatedJobDetails.salaryBudget"
          :disabled="isArchived"
          required
        />

        <InputGroupAddon class="min-w-fit">Currency</InputGroupAddon>
        <Dropdown
          v-model="updatedJobDetails.currency"
          :options="currencies"
          dropdown-icon="pi pi-angle-down"
          class="min-w-40"
          required
          :disabled="isArchived"
        />
      </InputGroup>
    </div>

    <div class="flex flex-col gap-2">
      <label for="description">Job Description</label>
      <Textarea
        v-model="updatedJobDetails.description"
        id="description"
        class="w-full"
        rows="4"
        cols="30"
        placeholder="Tell us what is the job about..."
        style="resize: none"
        required
        :disabled="isArchived"
      />
    </div>

    <div class="flex w-full flex-row gap-6">
      <div class="flex w-full flex-col gap-2">
        <label for="wantedCvs">Bonus Pay per CV</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-wallet" />
          </InputGroupAddon>
          <InputNumber id="bonusPayPerCv" v-model="updatedJobDetails.bonusPayPerCv" required :disabled="isArchived" />
        </InputGroup>
      </div>

      <div class="flex w-full flex-col gap-2">
        <label for="wantedCvs">Closure Bonus</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-money-bill" />
          </InputGroupAddon>
          <InputText id="closureBonus" v-model="updatedJobDetails.closureBonus" required :disabled="isArchived" />
        </InputGroup>
      </div>
    </div>
  </div>
</template>
