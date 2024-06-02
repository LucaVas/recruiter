<script setup lang="ts">
import CandidateTable from '@/components/candidacy/candidate/CandidateTable.vue';
import CandidacyHiringDetailsModal from '@/components/candidacy/header/CandidacyHiringDetailsModal.vue';
import { useToast } from 'primevue/usetoast';
import { onMounted } from 'vue';
import { getCandidacy, updateCandidacy } from '@/stores/candidacy/index';
import { useRoute, useRouter } from 'vue-router';
import HiringDetails from '@/components/candidacy/HiringDetails.vue';
import RemarksAndComments from '@/components/candidacy/RemarksAndComments.vue';
import CandidacyHeader from '@/components/candidacy/CandidacyHeader.vue';
import CandidacyFooter from '@/components/candidacy/CandidacyFooter.vue';
import Success from '@/components/Success.vue';
import { ApiError } from '@/utils/types';
import type { UpdateCandidacyRequest } from '@/stores/candidacy/schema';
import {
  jobId,
  pan,
  updatingCandidacy,
  candidacyUpdated,
  candidate,
  job,
  candidacy,
} from './index';
import { showError } from '@/utils/errorUtils';
import { headerModalOpen } from '../new-candidacy';
import { DEFAULT_SERVER_ERROR } from '@/consts';

const toast = useToast();
const router = useRouter();

async function update(candidacy: UpdateCandidacyRequest | undefined) {
  if (!candidacy || !jobId.value || pan.value === undefined) return;
  updatingCandidacy.value = true;
  try {
    await updateCandidacy(jobId.value, pan.value, candidacy);
    candidacyUpdated.value = true;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    updatingCandidacy.value = false;
  }
}

onMounted(async () => {
  const route = useRoute();
  jobId.value = Number(route.params.jobId);
  pan.value = route.params.pan as string;
  const res = await getCandidacy(jobId.value, pan.value);
  candidate.value = res.candidate;
  candidacy.value = res;
  job.value = res.job;
});
</script>
<template>
  <div class="flex w-full flex-col gap-8 pb-6">
    <div class="flex h-full w-full flex-col gap-6">
      <div v-if="job">
        <CandidacyHeader
          :status="job.status"
          :client="job.client.name"
          :name="job.name"
          @openModal="headerModalOpen = true"
        />
        <CandidacyHiringDetailsModal
          :visible="headerModalOpen"
          @close="headerModalOpen = false"
          :job="job"
        />
      </div>

      <CandidateTable v-if="candidate" :candidateToDisplay="candidate" @selectCandidate="null" />

      <HiringDetails
        v-if="candidacy"
        :candidacy="candidacy"
        @input="(details) => (candidacy = details)"
        :is-archived="false"
      />

      <RemarksAndComments
        v-if="candidacy"
        :candidacy="candidacy"
        @input="(details) => (candidacy = details)"
      />
    </div>

    <Success
      :visible="candidacyUpdated"
      title="Candidacy Updated"
      message="Candidacy updated successfully!"
      @close="{ candidacyUpdated = false; router.push({ name: 'CandidaciesPage' })}"
    />

    <CandidacyFooter
      v-if="job"
      :disabled="job.status === 'ARCHIVED'"
      :candidacySubmitted="candidacyUpdated"
      :submittingCandidacy="updatingCandidacy"
      :isUpdate="true"
      @update="update(candidacy)"
    />
  </div>
</template>
