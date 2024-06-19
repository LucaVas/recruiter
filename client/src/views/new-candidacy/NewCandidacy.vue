<script setup lang="ts">
import CandidateSection from '@/components/candidacy/CandidateSection.vue';
import CandidacyHiringDetailsModal from '@/components/candidacy/header/CandidacyHiringDetailsModal.vue';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';
import { getJob } from '@/stores/job';
import { useRoute, useRouter } from 'vue-router';
import { findCandidate } from '@/stores/candidate';
import { submitCandidacy } from '@/stores/candidacy';
import { ApiError } from '@/utils/types';
import HiringDetails from '@/components/candidacy/HiringDetails.vue';
import RemarksAndComments from '@/components/candidacy/RemarksAndComments.vue';
import FilesUploader from '@/components/uploader/FilesUploader.vue';
import CandidacyHeader from '@/components/candidacy/CandidacyHeader.vue';
import CandidacyFooter from '@/components/candidacy/CandidacyFooter.vue';
import type { Candidate, NewCandidate } from '@/stores/candidate/schema';
import Success from '@/components/Success.vue';
import {
  submittingNewCandidacy,
  candidacy,
  candidacySubmitted,
  searchedCandidate,
  candidateCreated,
  searchingForCandidate,
  selectedCandidate,
  job,
  candidate,
  headerModalOpen,
  resume,
} from './index';
import { showError } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';

const jobId = ref<number>();
const toast = useToast();
const router = useRouter();

async function submit(selectedCandidate: Candidate | null | undefined) {
  if (!selectedCandidate || !jobId.value) return;
  submittingNewCandidacy.value = true;
  try {
    await submitCandidacy(
      {
        ...candidacy.value,
        jobId: jobId.value,
        candidatePan: selectedCandidate.pan,
      },
      resume.value
    );
    candidacySubmitted.value = true;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    submittingNewCandidacy.value = false;
  }
}

async function searchCandidate(identifier: string) {
  searchingForCandidate.value = true;
  try {
    const res = await findCandidate(identifier);
    searchedCandidate.value = res.candidate;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    searchingForCandidate.value = false;
  }
}

function selectCandidate(
  candidate: Candidate | NewCandidate | null,
  searchedCandidate: Candidate | undefined
): void {
  if (!candidate || !searchedCandidate) return;
  if (candidate.pan === searchedCandidate.pan) selectedCandidate.value = searchedCandidate;
}

onMounted(async () => {
  const route = useRoute();
  jobId.value = Number(route.params.id);
  job.value = await getJob(jobId.value);
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

      <CandidateSection
        :searching="searchingForCandidate"
        :candidate="candidate"
        :searchedCandidate="searchedCandidate"
        :candidateCreated="candidateCreated"
        @searchCandidate="(identifier) => searchCandidate(identifier)"
        @selectCandidate="(candidate) => selectCandidate(candidate, searchedCandidate)"
      />

      <HiringDetails
        :candidacy="candidacy"
        @input="(details) => (candidacy = details)"
        :is-archived="false"
      />

      <RemarksAndComments
        :candidacy="candidacy"
        @input="(details) => (candidacy = details)"
        :is-archived="false"
      />

      <FilesUploader @addFile="(files: File[]) => (resume = files[0])" />
    </div>

    <Success
      :visible="candidacySubmitted"
      title="Candidacy submitted"
      message="Candidacy submitted successfully!"
      @close="{candidacySubmitted = false; router.push({ name: 'CandidaciesPage' })}"
    />

    <CandidacyFooter
      v-if="job"
      :disabled="job.status === 'ARCHIVED'"
      :candidacySubmitted="candidacySubmitted"
      :submittingCandidacy="submittingNewCandidacy"
      :isUpdate="false"
      @submit="submit(selectedCandidate)"
      @back="candidacySubmitted = false"
    />
  </div>
</template>