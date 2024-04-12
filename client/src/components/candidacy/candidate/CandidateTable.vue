<script setup lang="ts">
import CandidateSingleSelectTable from './CandidateSingleSelectTable.vue';
import type { RawCandidateDto } from '../../../stores/candidate/types';

const { candidateToDisplay } = defineProps<{
  candidateToDisplay: RawCandidateDto;
}>();
const emits = defineEmits<{
  (e: 'selectCandidate', candidate: RawCandidateDto | null): void;
}>();
</script>

<template>
  <div class="field p-fluid">
    <CandidateSingleSelectTable
      :candidates="[candidateToDisplay]"
      @selectCandidate="
        (candidate) =>
          emits('selectCandidate', candidate)
      "
    />
  </div>
</template>
