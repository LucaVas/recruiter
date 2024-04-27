<template>
  <div class="card flex flex-col gap-8">
    <ClientModal
      v-if="details"
      :isUpdate="false"
      :client="details.client"
      :visible="clientModalOpen"
      @close="clientModalOpen = false"
      @save="
        (client: Client) => {
          details.client = client;
          clientModalOpen = false;
        }
      "
    />
    <div class="flex w-full flex-col gap-2">
      <label class="text-sm" for="clientName">Client Name</label>
      <div class="flex sm:flex-row gap-3">
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-user"></i>
          </InputGroupAddon>
          <InputText
            id="clientName"
            v-model="details.client.name"
            @input="emit('input', details)"
            :disabled="disabled"
            :invalid="details.client.name === ''"
          />
        </InputGroup>
        <Button
          size="small"
          label="New client"
          icon="pi pi-user-plus"
          iconPos="right"
          class="min-w-fit"
          outlined
          @click="clientModalOpen = true"
        />
      </div>
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

    <div class="flex w-full flex-col gap-6 sm:flex-row">
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
            v-model="details.contractType"
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
import ClientModal from '@/components/client/ClientModal.vue';
import { ref } from 'vue';
import { jobStatuses, contractTypes } from './utils';
import type { Job, NewJobRequest } from '@/stores/job/schema';
import type { Client } from '@/stores/client/schema';

// props
const { jobDetails, disabled } = defineProps<{
  jobDetails: Job | NewJobRequest;
  disabled: boolean;
}>();

// emits
const emit = defineEmits<{
  (e: 'input', content: typeof details.value): void;
}>();

const clientModalOpen = ref(false);
const details = ref(jobDetails);
</script>
