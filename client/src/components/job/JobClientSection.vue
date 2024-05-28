<template>
  <div class="card flex flex-col gap-8">
    <ClientModal
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
            :client="selectedClient"
            @selectClient="(client) => { selectedClient = client; $emit('select', client) }"
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
  </div>
</template>

<script setup lang="ts">
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import ClientModal from '@/components/client/ClientModal.vue';
import { ref } from 'vue';
import ClientDropdown from './ClientDropdown.vue';
import { createClient } from '@/stores/client/index';
import type { Client, NewClient } from '@/stores/client/schema';
import { useToast } from 'primevue/usetoast';
import { handleError } from '@/utils/errorUtils';

const toast = useToast();

// props
const { client, clients } = defineProps<{
  client: Client;
  clients: Client[];
}>();

const create = async (client: NewClient) => {
  try {
    selectedClient.value = await createClient(client);
    emit('select', selectedClient.value);
    clientModalOpen.value = false;
  } catch (err) {
    handleError(toast, err);
  }
};

// emits
const emit = defineEmits<{
  (e: 'select', client: Client): void;
}>();

const clientModalOpen = ref(false);
const selectedClient = ref(client);
</script>
