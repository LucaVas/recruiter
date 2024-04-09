<script setup lang="ts">
import SearchCandidate from './SearchCandidate.vue';
import AddCandidate from './AddCandidate.vue';
import CandidateTable from './CandidateTable.vue';
import type { CandidateDto } from '@/stores/candidate/types';
import { ref } from 'vue';

const candidateToDisplay = ref<CandidateDto>();

const emits = defineEmits<{
  (e: 'passError', content: string): void;
  (e: 'selectCandidate', candidateId: number | null): void;
}>();
</script>

<template>
  <div class="flex w-full flex-col gap-2">
    <SearchCandidate
      @pass-error="(content: string) => emits('passError', content)"
      @pass-searched-candidate="(candidate: CandidateDto) => (candidateToDisplay = candidate)"
    />

    <AddCandidate
      @pass-error="(content: string) => emits('passError', content)"
      @pass-new-candidate="
        (candidate: CandidateDto) => {
          candidateToDisplay = candidate;
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
