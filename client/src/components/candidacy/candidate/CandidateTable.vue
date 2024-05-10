<script setup lang="ts">
import CandidateSingleSelectTable from './CandidateSingleSelectTable.vue';
import type { Candidate } from '@/stores/candidate/schema';

const { candidateToDisplay } = defineProps<{
  candidateToDisplay: Candidate;
}>();
const emits = defineEmits<{
  (e: 'selectCandidate', candidate: Candidate | null): void;
}>();
</script>

<template>
  <div class="field p-fluid">
    <CandidateSingleSelectTable
      :candidates="[candidateToDisplay]"
      @selectCandidate="(candidate) => emits('selectCandidate', candidate)"
    />
  </div>
</template>
