<script setup lang="ts">
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import { ref } from 'vue';

const { visible, creatingSkill } = defineProps<{
  visible: boolean;
  creatingSkill: boolean;
}>();

const emits = defineEmits<{
  (e: 'close'): void;
  (e: 'save', skill: { name: string }): void;
}>();

const form = ref({ name: '' });
</script>

<template>
  <div class="flex justify-center">
    <Dialog
      v-if="form"
      :visible="visible"
      @update:visible="$emit('close')"
      closeOnEscape
      modal
      header="New Question"
      class="w-[90%] sm:w-2/3 md:w-2/3 lg:w-1/3"
    >
      <div class="mb-5 flex flex-col gap-2 py-4">
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-tag"></i>
          </InputGroupAddon>
          <InputText placeholder="Name" autocomplete="off" v-model="form.name" />
        </InputGroup>
      </div>

      <div class="flex justify-end gap-2">
        <Button
          label="Cancel"
          severity="secondary"
          @click="$emit('close')"
          :loading="creatingSkill"
          :disabled="creatingSkill"
        />
        <Button
          label="Save"
          @click="emits('save', form)"
          :loading="creatingSkill"
          :disabled="creatingSkill"
        />
      </div>
    </Dialog>
  </div>
</template>
