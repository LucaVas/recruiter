<script setup lang="ts">
import SearchCandidate from './candidate/SearchCandidate.vue';
import CandidateTable from './candidate/CandidateTable.vue';
import type { CandidateDto } from '@/stores/candidate/types';
import { ref } from 'vue';
import NewCandidateModal from './candidate/new-candidate/NewCandidateModal.vue';

const candidateToDisplay = ref<CandidateDto>();
const newCandidateModalOpen = ref(false);

const emits = defineEmits<{
  (e: 'passError', content: string): void;
  (e: 'selectCandidate', candidateId: number | null): void;
}>();

</script>

<template>
  <div class="flex w-full flex-col gap-2">
    <label class="text-sm">Search for a candidate</label>
    <NewCandidateModal
      :visible="newCandidateModalOpen"
      @close="newCandidateModalOpen = false"
      @selectCandidate="
        (newCandidate: CandidateDto) => {
          newCandidateModalOpen = false;
          emits('selectCandidate', newCandidate.id);
        }
      "
    />

    <SearchCandidate
      @pass-error="(content: string) => emits('passError', content)"
      @pass-searched-candidate="(candidate: CandidateDto) => (candidateToDisplay = candidate)"
      @openNewCandidateModal="newCandidateModalOpen = true"
    />

    <CandidateTable
      v-if="candidateToDisplay"
      :candidate-to-display="candidateToDisplay"
      @pass-candidate-selected-id="(id) => emits('selectCandidate', id)"
    />
  </div>
</template>
