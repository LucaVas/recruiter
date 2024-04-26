<script setup lang="ts">
import Button from 'primevue/button';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import { ref } from 'vue';

const { filters, showColumns } = defineProps<{
  filters: any;
  showColumns: boolean;
}>();

defineEmits<{
  (e: 'clearFilter'): void;
  (e: 'showOrHideColumns'): void;
}>();

const dataFilters = ref(filters);
</script>

<template>
  <div
    class="flex flex-col items-start justify-start gap-3 sm:flex-row sm:items-center sm:justify-between"
  >
    <div class="flex w-full flex-row items-center justify-start gap-4 p-0">
      <Button
        type="button"
        icon="pi pi-filter-slash"
        size="small"
        outlined
        @click="$emit('clearFilter')"
      />
      <Button
        type="button"
        size="small"
        :label="showColumns ? 'Hide Columns' : 'Show Columns'"
        @click="$emit('showOrHideColumns')"
      />
    </div>
    <div class="flex w-full items-center sm:justify-end">
      <IconField iconPosition="left">
        <InputIcon>
          <i class="pi pi-search" />
        </InputIcon>
        <InputText
          v-model="dataFilters['global'].value"
          size="small"
          placeholder="Keyword Search"
          class="w-80"
        />
      </IconField>
    </div>
  </div>
</template>
