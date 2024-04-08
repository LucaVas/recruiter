<script setup lang="ts">
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputText from 'primevue/inputtext';
import Dropdown from 'primevue/dropdown';
import type { JobDto } from '../../stores/job/types';
import { ref, onMounted } from 'vue';
import { jobStatuses, contractTypes } from './utils';

const { jobDetails } = defineProps<{
  jobDetails: JobDto;
}>();
const updatedJobDetails = ref<Partial<JobDto>>({
  client: '',
  status: 'OPEN',
  contractType: { contractTypeName: 'PERMANENT' },
});

onMounted(() => {
  updatedJobDetails.value = jobDetails;
});
</script>

<template>
  <div class="card flex flex-col gap-8">
    <div class="flex w-full flex-col gap-2">
      <label for="clientName">Client Name</label>
      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-user"></i>
        </InputGroupAddon>
        <InputText id="clientName" v-model="updatedJobDetails.client" />
      </InputGroup>
    </div>

    <div class="flex w-full flex-col gap-2">
      <label for="jobName">Job Name</label>
      <InputGroup>
        <InputGroupAddon> <i class="pi pi-briefcase"></i></InputGroupAddon>
        <InputText id="jobName" v-model="updatedJobDetails.name" />
      </InputGroup>
    </div>

    <div class="flex w-full flex-row gap-6">
      <div class="flex w-full flex-col gap-2">
        <label for="jobStatus">Job Status</label>
        <InputGroup>
          <Dropdown
            v-model="updatedJobDetails.status"
            :options="jobStatuses"
            optionLabel="name"
            optionValue="value"
            id="jobStatus"
            class="w-full"
            @update:modelValue="(value) => console.log(value.value)"
          />
        </InputGroup>
      </div>

      <div v-if="updatedJobDetails.contractType" class="flex w-full flex-col gap-2">
        <label for="jobStatus">Contract type</label>
        <InputGroup>
          <Dropdown
            v-model="updatedJobDetails.contractType.contractTypeName"
            :options="contractTypes"
            optionLabel="name"
            optionValue="value"
            class="w-full"
          />
        </InputGroup>
      </div>
    </div>
  </div>
</template>
