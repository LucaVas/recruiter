<template>
  <div v-if="questions">
    <DataTable
      v-model:selection="selectedQuestions"
      :value="questions"
      dataKey="id"
      tableStyle="min-width: 50rem"
      size="small"
      paginator
      :rows="5"
      :rowsPerPageOptions="[5, 10, 20, 50]"
    >
      <Column selectionMode="multiple" headerStyle="width: 3rem"></Column>
      <Column field="text" header="Question"></Column>
    </DataTable>
  </div>
</template>

<script setup lang="ts">
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { type Question } from '@/stores/question/schema';
import { ref } from 'vue';

const { questions } = defineProps<{
  questions: Question[] | undefined;
}>();

const selectedQuestions = ref<Question[]>();
</script>
