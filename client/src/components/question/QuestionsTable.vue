<template>
  <div v-if="questions && visible">
    <DataTable
      :value="questions"
      stripedRows
      tableStyle="min-width: 50rem"
      size="small"
      paginator
      :rows="5"
      :rowsPerPageOptions="[5, 10, 20, 50]"
    >
      <Column style="width: 1rem">
        <template #body="{ data }">
          <div class="flex items-center justify-center">
            <Button icon="pi pi-trash" class="p-panel-header-icon" @click="$emit('removeQuestion', data)" unstyled />
          </div>
        </template>
      </Column>
      <Column field="title" header="Title"></Column>
      <Column field="text" header="Text"></Column>
    </DataTable>
  </div>
</template>

<script setup lang="ts">
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { type Question } from '@/stores/question/schema';

const { questions, visible } = defineProps<{
  questions: Question[] | undefined;
  visible: boolean;
}>();

defineEmits<{
  (e: 'removeQuestion', question: Question): void;
}>();
</script>
