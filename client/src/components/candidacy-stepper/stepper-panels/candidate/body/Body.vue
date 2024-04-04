<script setup lang="ts">
import SearchCandidate from './SearchCandidate.vue';
import AddCandidate from './AddCandidate.vue';
import CandidateTable from './CandidateTable.vue';
import type { CandidateDto } from '@/stores/candidate/types';
import { ref } from 'vue';

const candidateToDisplay = ref<CandidateDto>();

const emits = defineEmits<{
  (e: 'increaseIndex'): void;
  (e: 'passError', content: string): void;
  (e: 'selectCandidate', candidateId: number | null): void;
}>();
</script>

<template>
  <div class="flex w-full flex-col gap-6">
    <div class="mb-3 mt-3 text-center text-xl font-semibold">Candidate information</div>

    <SearchCandidate
      @pass-error="(content: string) => emits('passError', content)"
      @pass-searched-candidate="(candidate: CandidateDto) => (candidateToDisplay = candidate)"
    />

    <AddCandidate
      @pass-error="(content: string) => emits('passError', content)"
      @pass-new-candidate="
        (candidate: CandidateDto) => {
          candidateToDisplay = candidate;
          emits('increaseIndex');
        }
      "
    />

    <CandidateTable
      v-if="candidateToDisplay"
      :candidate-to-display="candidateToDisplay"
      @pass-candidate-selected-id="(id) => emits('selectCandidate', id)"
    />
  </div>
</template>
