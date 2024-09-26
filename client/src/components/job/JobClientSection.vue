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
          <div class="flex w-full justify-center">
            <Dropdown
              :modelValue="selectedClient"
              :options="clients"
              @update:model-value="
                (client: Client) => {
                  selectedClient = client;
                  $emit('select', client);
                }
              "
              filter
              optionLabel="name"
              placeholder="Select a Client"
              class="md:w-14rem w-full"
            >
              <template #value="slotProps">
                <div v-if="slotProps.value" class="flex items-center">
                  <div>{{ slotProps.value.name }}</div>
                </div>
              </template>
              <template #option="slotProps">
                <div class="align-items-center flex">
                  <div>{{ slotProps.option.name }}</div>
                </div>
              </template>
            </Dropdown>
          </div>
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
import ClientModal from '@/components/modals/ClientModal.vue';
import { ref } from 'vue';
import { createClient } from '@/api/clientApi';
import type { Client, NewClient } from '@/types/clientTypes';
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
