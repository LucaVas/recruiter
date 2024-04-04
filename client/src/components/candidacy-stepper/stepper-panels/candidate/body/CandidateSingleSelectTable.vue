<script setup lang="ts">
import { ref } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import type { CandidateDto } from '@/stores/candidate/types';

const { candidates } = defineProps<{
  candidates?: CandidateDto[];
}>();
defineEmits<{
  (e: 'selectCandidate', selectedCandidate: CandidateDto): void;
}>();

const selectedCandidate = ref();
</script>

<template>
  <DataTable
    v-model:selection="selectedCandidate"
    @update:selection="$emit('selectCandidate', selectedCandidate)"
    :value="candidates"
    selectionMode="single"
    dataKey="id"
    tableStyle="min-width: 50rem"
  >
    <Column field="name" header="Name"></Column>
    <Column field="education" header="Education"></Column>
    <Column field="totalExperience" header="Total Experience"></Column>
    <Column field="currentCtc" header="Current CTC"></Column>
  </DataTable>
</template>
