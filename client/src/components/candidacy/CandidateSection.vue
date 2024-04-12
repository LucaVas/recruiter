<script setup lang="ts">
import SearchCandidate from './candidate/SearchCandidate.vue';
import CandidateTable from './candidate/CandidateTable.vue';
import { ref, onMounted } from 'vue';
import CandidateModal from './candidate/shared/CandidateModal.vue';
import type { CandidateForm } from '../../views/candidacy/index';
import type { RawCandidateDto } from '../../stores/candidate/types';

// variables
const details = ref<CandidateForm>();
const newCandidateModalOpen = ref(false);

// props
const { candidate, searchedCandidate, searching } = defineProps<{
  candidate: CandidateForm;
  searchedCandidate: RawCandidateDto | undefined;
  searching: boolean;
}>();

// emits
const emits = defineEmits<{
  (e: 'passError', content: string): void;
  (e: 'selectCandidate', candidate: RawCandidateDto | null): void;
  (e: 'update', content: CandidateForm): void;
  (e: 'searchCandidate', identifier: string): void;
}>();

// init
onMounted(() => {
  details.value = candidate;
});
</script>

<template>
  <div class="flex w-full flex-col gap-2">
    <label class="text-sm">Search for a candidate</label>
    <CandidateModal
      v-if="details"
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
