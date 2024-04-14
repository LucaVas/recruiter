<script setup lang="ts">
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import Dropdown from 'primevue/dropdown';
import Textarea from 'primevue/textarea';
import type { Currency } from '@/stores/job/types';
import { ref } from 'vue';
import type { Job, NewJobRequest } from '@/stores/job/types';

// props
const { jobDetails, disabled } = defineProps<{
  jobDetails: Job | NewJobRequest;
  disabled: boolean;
}>();

// emits
const emit = defineEmits<{
  (e: 'input', content: typeof details.value): void;
}>();

// variables
const details = ref(jobDetails);
const currencies = ref<Currency[]>(['INR']);
</script>

<template>
  <div class="flex flex-col gap-8">
    <div class="flex w-full flex-row gap-6">
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm" for="wantedCvs">Wanted CVs</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-file" />
          </InputGroupAddon>
          <InputNumber
            id="wantedCvs"
            :min="0"
            v-model="details.wantedCvs"
            required
            :disabled="disabled"
            @input="emit('input', details)"
          />
        </InputGroup>
      </div>
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm" for="noticePeriodInDays">Notice Period</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-calendar-times" />
          </InputGroupAddon>
          <InputNumber
            id="noticePeriodInDays"
            :min="0"
            v-model="details.noticePeriodInDays"
            required
            :disabled="disabled"
            @input="emit('input', details)"
          />
          <InputGroupAddon class="min-w-fit">days</InputGroupAddon>
        </InputGroup>
      </div>
    </div>

    <div class="flex w-full flex-col gap-2">
      <label class="text-sm" for="experienceRange">Experience range</label>
      <div class="flex flex-row gap-6">
        <div class="w-full">
          <InputGroup>
            <InputGroupAddon class="min-w-fit"> From </InputGroupAddon>
            <InputNumber
              id="experienceRangeMin"
              :min="0"
              v-model="details.experienceRangeMin"
              required
              :disabled="disabled"
              @input="emit('input', details)"
            />
            <InputGroupAddon class="min-w-fit"> Years </InputGroupAddon>
          </InputGroup>
        </div>
        <div class="w-full">
          <InputGroup>
            <InputGroupAddon class="min-w-fit"> To </InputGroupAddon>
            <InputNumber
              id="experienceRangeMax"
              :min="0"
              v-model="details.experienceRangeMax"
              required
              :disabled="disabled"
              @input="emit('input', details)"
            />
            <InputGroupAddon class="min-w-fit"> Years </InputGroupAddon>
          </InputGroup>
        </div>
      </div>
    </div>

    <div class="flex w-full flex-col gap-2">
      <label class="text-sm" for="experienceRange">Salary Budget</label>
      <InputGroup class="w-full">
        <InputGroupAddon>
          <i class="pi pi-money-bill" />
        </InputGroupAddon>
        <InputNumber
          class="w-full"
          id="salaryBudget"
          :min="0"
          v-model="details.salaryBudget"
          required
          :disabled="disabled"
          @input="emit('input', details)"
        />

        <InputGroupAddon class="min-w-fit">Currency</InputGroupAddon>
        <Dropdown
          v-model="details.currency"
          :options="currencies"
          dropdown-icon="pi pi-angle-down"
          class="min-w-40"
          @change="emit('input', details)"
          required
          :disabled="disabled"
        />
      </InputGroup>
    </div>

    <div class="flex flex-col gap-2">
      <label class="text-sm" for="description">Job Description</label>
      <Textarea
        v-model="details.description"
        id="description"
        class="w-full"
        rows="4"
        cols="30"
        placeholder="Tell us what is the job about..."
        style="resize: none"
        required
        :disabled="disabled"
        @input="emit('input', details)"
        :invalid="details.description === ''"
      />
    </div>
  </div>
</template>
