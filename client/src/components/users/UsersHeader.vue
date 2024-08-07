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
  (e: 'addNewUser'): void;
}>();

const dataFilters = ref(filters);
</script>

<template>
  <div class="flex items-center justify-between gap-3">
    <div class="flex gap-3">
      <Button
        type="button"
        icon="pi pi-filter-slash"
        size="small"
        outlined
        @click="$emit('clearFilter')"
      />
      <div>
        <Button
          icon="pi pi-plus"
          size="small"
          outlined
          class="md:hidden"
          @click="$emit('addNewUser')"
        />
        <Button
          icon="pi pi-plus"
          label="New"
          outlined
          class="hidden min-w-fit md:flex"
          size="small"
          @click="$emit('addNewUser')"
        />
      </div>
    </div>
    <IconField iconPosition="left">
      <InputIcon>
        <i class="pi pi-search" />
      </InputIcon>
      <InputText
        v-model="dataFilters['global'].value"
        size="small"
        placeholder="Search by email or username"
        class="w-full sm:w-80"
      />
    </IconField>
  </div>
</template>
