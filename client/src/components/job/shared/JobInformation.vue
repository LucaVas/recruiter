<template>
  <div class="card flex flex-col gap-8">
    <div class="flex w-full flex-col gap-2">
      <label class="text-sm" for="clientName">Client Name</label>
      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-user"></i>
        </InputGroupAddon>
        <InputText
          id="clientName"
          v-model="details.client"
          @input="emit('input', details)"
          :disabled="disabled"
          :invalid="details.client === ''"
        />
      </InputGroup>
    </div>

    <div class="flex w-full flex-col gap-2">
      <label class="text-sm" for="jobName">Job Name</label>
      <InputGroup>
        <InputGroupAddon> <i class="pi pi-briefcase"></i></InputGroupAddon>
        <InputText
          id="jobName"
          v-model="details.name"
          @input="emit('input', details)"
          :disabled="disabled"
          :invalid="details.name === ''"
        />
      </InputGroup>
    </div>

    <div class="flex w-full flex-row gap-6">
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm" for="jobStatus">Job Status</label>
        <InputGroup>
          <Dropdown
            v-model="details.status"
            :options="jobStatuses"
            optionLabel="name"
            optionValue="value"
            id="jobStatus"
            class="w-full"
            @change="emit('input', details)"
            :disabled="disabled"
          />
        </InputGroup>
      </div>

      <div v-if="details.contractType" class="flex w-full flex-col gap-2">
        <label class="text-sm" for="jobStatus">Contract type</label>
        <InputGroup>
          <Dropdown
            v-model="details.contractType.contractTypeName"
            :options="contractTypes"
            optionLabel="name"
            optionValue="value"
            class="w-full"
            @change="emit('input', details)"
            :disabled="disabled"
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
import Dropdown from 'primevue/dropdown';
import { ref } from 'vue';
import { jobStatuses, contractTypes } from './utils';
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

const details = ref(jobDetails);
</script>
