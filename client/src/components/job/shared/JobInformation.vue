<template>
  <div class="card flex flex-col gap-8">
    <ClientModal
      v-if="details"
      :visible="clientModalOpen"
      @close="clientModalOpen = false"
      @save="(client: NewClient) => create(client)"
    />
    <div class="flex w-full flex-col gap-2">
      <label class="text-sm" for="clientName">Client Name</label>
      <div class="flex gap-3 sm:flex-row">
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-user"></i>
          </InputGroupAddon>
          <ClientDropdown
            :clients="clients"
            :client="details.client"
            @selectClient="(client) => (details.client = client)"
          />
        </InputGroup>
        <Button
          size="small"
          icon="pi pi-user-plus"
          iconPos="right"
          class="min-w-fit md:hidden"
          outlined
          @click="clientModalOpen = true"
        />
        <Button
          size="small"
          label="New client"
          icon="pi pi-user-plus"
          iconPos="right"
          class="hidden min-w-fit md:flex"
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
import ClientDropdown from './ClientDropdown.vue';
import { createClient } from '@/stores/client/index';
import type { NewClient } from '@/stores/client/schema';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '@/utils/types';
import { showError } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';

const toast = useToast();

// props
const { jobDetails, clients, disabled } = defineProps<{
  jobDetails: Job | NewJobRequest;
  clients: Client[];
  disabled: boolean;
}>();

const create = async (client: NewClient) => {
  try {
    const newClient = await createClient(client);
    emit('selectClient', newClient);
    clientModalOpen.value = false;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  }
};

// emits
const emit = defineEmits<{
  (e: 'input', content: typeof details.value): void;
  (e: 'selectClient', client: Client): void;
}>();

const clientModalOpen = ref(false);
const details = ref(jobDetails);
</script>
