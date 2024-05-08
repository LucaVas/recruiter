<template>
  <div class="justify-center flex w-full">
    <Dropdown
      v-model="selectedClient"
      :options="clients"
      filter
      optionLabel="name"
      placeholder="Select a Client"
      class="md:w-14rem w-full"
    >
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

const { clients } = defineProps<{
  clients: Client[];
}>();

const selectedClient = ref<Client | null>(null);

defineEmits<{
  (e: 'selectClient', client: Client): void;
}>();
</script>
