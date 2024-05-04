<script setup lang="ts">
import Button from 'primevue/button';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import { ref } from 'vue';

const { filters } = defineProps<{
  filters: any;
}>();

defineEmits<{
  (e: 'clearFilter'): void;
}>();

const dataFilters = ref(filters);
</script>

<template>
  <div class="flex items-center justify-between gap-3">
    <Button
      type="button"
      icon="pi pi-filter-slash"
      size="small"
      outlined
      @click="$emit('clearFilter')"
    />
    <IconField iconPosition="left">
      <InputIcon>
        <i class="pi pi-search" />
      </InputIcon>
      <InputText
        v-model="dataFilters['global'].value"
        size="small"
        placeholder="Keyword Search"
        class="w-full sm:w-80"
      />
    </IconField>
  </div>
</template>
