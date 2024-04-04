<script setup lang="ts">
import type { CandidateDto } from '../../../../../stores/candidate/types';
import CandidateSingleSelectTable from './CandidateSingleSelectTable.vue';

const { candidateToDisplay } = defineProps<{
  candidateToDisplay: CandidateDto;
}>();
const emits = defineEmits<{
  (e: 'passCandidateSelectedId', id: number | null): void;
}>();
</script>

<template>
  <div class="field p-fluid">
    <CandidateSingleSelectTable
      :candidates="[candidateToDisplay]"
      @selectCandidate="
        (candidate) =>
          emits('passCandidateSelectedId', candidate === null ? candidate : candidate.id)
      "
    />
  </div>
</template>
