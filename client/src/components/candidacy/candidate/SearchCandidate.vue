<script setup lang="ts">
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import { ref } from 'vue';
import { findCandidate } from '@/stores/candidate';
import type { CandidateDto, CandidateResponse } from '@/stores/candidate/types';
import { ApiError } from '@/utils/types';

const emits = defineEmits<{
  (e: 'passError', content: string): void;
  (e: 'passSearchedCandidate', candidate: CandidateDto): void;
  (e: 'openNewCandidateModal'): void;
}>();

const candidateSearchLoading = ref(false);
const identifier = ref('');

const searchCandidate = async () => {
  candidateSearchLoading.value = true;
  try {
    const res = (await findCandidate(identifier.value)) as CandidateResponse;
    emits('passSearchedCandidate', res.candidate);
  } catch (err) {
    if (err instanceof ApiError) emits('passError', err.message);
  } finally {
    candidateSearchLoading.value = false;
  }
};
</script>

<template>
  <div class="flex flex-col gap-2">
    <div class="flex flex-col gap-2 sm:flex-row">
      <IconField class="w-full">
        <InputIcon>
          <i class="pi pi-search" />
        </InputIcon>
        <InputText
          id="identifier"
          v-model="identifier"
          type="text"
          class="w-full"
          placeholder="Candidate PAN, phone or email"
          required
        />
      </IconField>
      <Button
        label="Search"
        size="small"
        class="w-full sm:w-[10rem]"
        :loading="candidateSearchLoading"
        @click="searchCandidate()"
        :disabled="identifier === ''"
      />
      <Button
        size="small"
        label="New candidate"
        icon="pi pi-user-plus"
        iconPos="right"
        class="min-w-fit"
        outlined
        @click="$emit('openNewCandidateModal')"
      />
    </div>
  </div>
</template>
