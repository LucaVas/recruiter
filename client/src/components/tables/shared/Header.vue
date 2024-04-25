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
  <div class="flex flex-col items-center justify-between sm:flex-row gap-3">
    <div class="flex flex-row items-center justify-between w-full p-0">
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
    <div class="flex items-center justify-end">
      <IconField iconPosition="left">
        <InputIcon>
          <i class="pi pi-search" />
        </InputIcon>
        <InputText v-model="dataFilters['global'].value" placeholder="Keyword Search" />
      </IconField>
    </div>
  </div>
</template>
