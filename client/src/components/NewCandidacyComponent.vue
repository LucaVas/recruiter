<script setup lang="ts">
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import Button from 'primevue/button';
import { ref } from 'vue';
import { useCandidateStore } from '../stores/candidate/index';
import CandidateSingleSelectTable from './CandidateSingleSelectTable.vue';
import type { CandidateDto } from '../stores/candidate/types';
import { ApiError } from '../utils/types';
import NewCandidateModal from './new-candidate/NewCandidateModal.vue';

const candidateStore = useCandidateStore();

// candidate search scripts
const candidateSelectedId = ref<number | null>();
const candidatePanSearch = ref('');
const candidateSearchError = ref('');
const candidateToDisplay = ref<CandidateDto>();
const candidateSearchLoading = ref(false);
const searchCandidate = async () => {
  candidateSearchLoading.value = true;
  try {
    const res = await candidateStore.findCandidateByPan(candidatePanSearch.value);
    candidateToDisplay.value = res.candidate;
  } catch (err) {
    if (err instanceof ApiError) candidateSearchError.value = err.message;
  } finally {
    candidateSearchLoading.value = false;
  }
};
const newCandidateModalOpen = ref(false);
</script>

<template>
  <div class="mb-5 flex h-full w-[50rem] flex-col justify-between gap-6">
    <div class="flex w-full flex-col gap-6">
      <div class="mb-3 mt-3 text-center text-xl font-semibold">Candidate information</div>

      <h3>Search for a candidate</h3>
      <div class="field p-fluid flex gap-2">
        <IconField class="w-full">
          <InputIcon>
            <i class="pi pi-search" />
          </InputIcon>
          <InputText
            id="candidatePan"
            v-model="candidatePanSearch"
            type="text"
            placeholder="Candidate PAN"
            required
          />
        </IconField>
        <Button
          class="w-[10rem]"
          type="button"
          label="Search"
          icon="pi pi-search"
          :loading="candidateSearchLoading"
          @click="searchCandidate()"
          :disabled="candidatePanSearch === ''"
        />
      </div>

      <h3>Add a new candidate</h3>

      <!-- create new candidate -->
      <div class="field p-fluid">
        <Button
          label="New candidate"
          icon="pi pi-user-plus"
          iconPos="right"
          @click="newCandidateModalOpen = true"
        />
      </div>

      <NewCandidateModal
        v-if="newCandidateModalOpen"
        @close="newCandidateModalOpen = false"
        @selectCandidate="
          (newCandidate) => {
            candidateToDisplay = newCandidate;
            newCandidateModalOpen = false;
          }
        "
      />

      <Message v-if="candidateSearchError" severity="error" :closable="false">{{
        candidateSearchError
      }}</Message>

      <div v-if="candidateToDisplay !== undefined" class="field p-fluid">
        <CandidateSingleSelectTable
          :candidates="[candidateToDisplay]"
          @selectCandidate="
            (candidate) =>
              candidate === null
                ? (candidateSelectedId = candidate)
                : (candidateSelectedId = candidate.id)
          "
        />
      </div>
    </div>
    <div class="flex justify-end pt-4">
      <Button
        label="Next"
        icon="pi pi-arrow-right"
        iconPos="right"
        @click="null"
        :disabled="false"
      />
    </div>
  </div>
</template>
