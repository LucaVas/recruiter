<script setup lang="ts">
import SearchCandidate from './candidate/SearchCandidate.vue';
import CandidateTable from './candidate/CandidateTable.vue';
import { ref } from 'vue';
import CandidateModal from './candidate/shared/CandidateModal.vue';
import type { Candidate } from '@/stores/candidate/types';
import type { NewCandidateRequest } from '@/stores/candidate/types';

// props
const { candidate, searchedCandidate, searching, candidateCreated } = defineProps<{
  candidate: Candidate | NewCandidateRequest;
  searchedCandidate: Candidate | undefined;
  searching: boolean;
  candidateCreated: boolean;
}>();

// emits
const emits = defineEmits<{
  (e: 'passError', content: string): void;
  (e: 'selectCandidate', candidate: NewCandidateRequest | null): void;
  (e: 'update', content: NewCandidateRequest): void;
  (e: 'searchCandidate', identifier: string): void;
}>();

// variables
const details = ref(candidate);
const newCandidateModalOpen = ref(false);
</script>

<template>
  <div class="flex w-full flex-col gap-2">
    <label class="text-sm">Search for a candidate</label>
    <CandidateModal
      v-if="details"
      :isUpdate="false"
      :candidate="details"
      :visible="newCandidateModalOpen"
      @close="newCandidateModalOpen = false"
      @save="
        emits('update', details);
        newCandidateModalOpen = false;
      "
      @update="(candidate) => (details = candidate)"
    />

    <SearchCandidate
      @search="(identifier: string) => emits('searchCandidate', identifier)"
      @openNewCandidateModal="newCandidateModalOpen = true"
      :searching="searching"
    />

    <CandidateTable
      v-if="searchedCandidate"
      :candidateToDisplay="searchedCandidate"
      @selectCandidate="(candidate) => emits('selectCandidate', candidate)"
    />
  </div>
</template>
@/stores/candidate/schema@/stores/candidate/schema
