<script setup lang="ts">
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import { ref } from 'vue';
import { type Client, industries } from '@/stores/client/schema';

const { client, visible, isUpdate } = defineProps<{
  client: Client | undefined;
  visible: boolean;
  isUpdate: boolean;
}>();

const emits = defineEmits<{
  (e: 'close'): void;
  (e: 'save', content: Client): void;
}>();

const clientForm = ref(client);
</script>

<template>
  <div class="card flex justify-center">
    <Dialog
      v-if="clientForm"
      :visible="visible"
      @update:visible="$emit('close')"
      closeOnEscape
      modal
      header="New Client"
      class="w-[90%] sm:w-2/3 md:w-2/3 lg:w-1/3"
    >
      <div class="mb-5 flex flex-col gap-2">
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-briefcase"></i>
          </InputGroupAddon>
          <InputText
            placeholder="Name"
            autocomplete="off"
            v-model="clientForm.name"
            :invalid="clientForm.name === ''"
          />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-building"></i>
          </InputGroupAddon>
          <Dropdown
            v-model="clientForm.industry"
            :options="industries"
            optionLabel="name"
            placeholder="Select an Industry"
            class="md:w-14rem w-full"
          />
        </InputGroup>
      </div>

      <div class="flex justify-end gap-2">
        <Button label="Cancel" severity="secondary" @click="$emit('close')" />
        <Button label="Save" @click="emits('save', clientForm)" />
      </div>
    </Dialog>
  </div>
</template>
