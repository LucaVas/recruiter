<script setup lang="ts">
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import { ref } from 'vue';
import { findCandidateByPan } from '@/stores/candidate';
import type { CandidateDto, CandidateResponse } from '@/stores/candidate/types';
import { ApiError } from '@/utils/types';


const emits = defineEmits<{
  (e: 'passError', content: string): void;
  (e: 'passSearchedCandidate', candidate: CandidateDto): void;
}>();

const candidateSearchLoading = ref(false);
const candidatePanSearch = ref('');

const searchCandidate = async () => {
  candidateSearchLoading.value = true;
  try {
    const res = await findCandidateByPan(candidatePanSearch.value) as CandidateResponse;
    emits('passSearchedCandidate', res.candidate);
  } catch (err) {
    if (err instanceof ApiError) emits('passError', err.message);
  } finally {
    candidateSearchLoading.value = false;
  }
};
</script>

<template>
  <div>
    <div class="text-lg font-semibold">Search for a candidate</div>
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
  </div>
</template>
