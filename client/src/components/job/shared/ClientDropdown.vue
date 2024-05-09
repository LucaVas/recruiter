<template>
  <div class="flex w-full justify-center">
    <Dropdown
      :modelValue="client"
      :options="clients"
      @update:modelValue="$emit('selectClient', selectedClient)"
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
</template>

<script setup lang="ts">
import Dropdown from 'primevue/dropdown';
import type { Client } from '@/stores/client/schema';
import { ref } from 'vue';

const { clients, client } = defineProps<{
  clients: Client[];
  client: Client;
}>();

const selectedClient = ref(client);

defineEmits<{
  (e: 'selectClient', client: Client): void;
}>();
</script>
