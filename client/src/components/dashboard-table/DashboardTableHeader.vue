<script setup lang="ts">
import Button from 'primevue/button';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import { filters } from './utils';

defineEmits<{
  (e: 'clearFilter'): void;
}>();
</script>

<template>
  <div class="flex items-center justify-between">
    <Button
      type="button"
      icon="pi pi-filter-slash"
      size="small"
      label="Clear"
      outlined
      @click="$emit('clearFilter')"
    />
    <div class="flex items-center justify-end">
      <IconField iconPosition="left">
        <InputIcon>
          <i class="pi pi-search" />
        </InputIcon>
        <InputText v-model="filters['global'].value" placeholder="Keyword Search" />
      </IconField>
    </div>
  </div>
</template>
