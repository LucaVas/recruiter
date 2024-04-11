<script setup lang="ts">
import SearchCandidate from './candidate/SearchCandidate.vue';
import CandidateTable from './candidate/CandidateTable.vue';
import { ref, onMounted } from 'vue';
import NewCandidateModal from './candidate/new-candidate/NewCandidateModal.vue';
import type { CandidateForm } from '../../views/candidacy/index';

const details = ref<CandidateForm>();
const newCandidateModalOpen = ref(false);

// props
const { candidate } = defineProps<{
  candidate: CandidateForm;
}>();

// emits
const emits = defineEmits<{
  (e: 'passError', content: string): void;
  (e: 'selectCandidate', candidateId: number | null): void;
  (e: 'update'): void;
}>();

// init
onMounted(() => {
  details.value = candidate;
});
</script>

<template>
  <div class="flex w-full flex-col gap-2">
    <label class="text-sm">Search for a candidate</label>
    <NewCandidateModal
      v-if="details"
      :candidate="details"
      :visible="newCandidateModalOpen"
      @close="newCandidateModalOpen = false"
      @save="emits('update')"
      @update="(candidate) => (details = candidate)"
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
