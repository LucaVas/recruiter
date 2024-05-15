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
  (e: 'refresh'): void;
}>();

const dataFilters = ref(filters);
</script>

<template>
  <div class="flex w-full items-center justify-between">
    <Button type="button" icon="pi pi-sync" size="small" outlined @click="$emit('refresh')" />
    <IconField iconPosition="left">
      <InputIcon>
        <i class="pi pi-search" />
      </InputIcon>
      <InputText
        v-model="dataFilters['global'].value"
        size="small"
        placeholder="Keyword Search"
        class="w-full"
      />
    </IconField>
  </div>
</template>
