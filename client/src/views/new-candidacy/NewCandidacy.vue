<template>
  <div class="flex w-full flex-col gap-8 pb-6">
    <div v-if="!candidacySubmitted" class="flex h-full w-full flex-col gap-6">
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
        @update="(candidateForm) => createNewCandidate(candidateForm)"
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

      <FilesUploader />
    </div>

    <div v-else class="flex h-full w-full items-center justify-center">
      <Success :message="'Candidacy submitted successfully!'" />
    </div>

    <CandidacyFooter
      v-if="job"
      :disabled="job.status === 'ARCHIVED'"
      :candidacySubmitted="candidacySubmitted"
      :submittingCandidacy="submittingNewCandidacy"
      :isUpdate="false"
      @submit="submit(selectedCandidate)"
    />
  </div>
</template>

<script setup lang="ts">
import CandidateSection from '@/components/candidacy/CandidateSection.vue';
import CandidacyHiringDetailsModal from '@/components/candidacy/header/CandidacyHiringDetailsModal.vue';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';
import { getJob } from '@/stores/job';
import { useRoute } from 'vue-router';
import { findCandidate } from '@/stores/candidate';
import { submitCandidacy } from '@/stores/candidacy';
import { ApiError } from '@/utils/types';
import HiringDetails from '@/components/candidacy/HiringDetails.vue';
import RemarksAndComments from '@/components/candidacy/RemarksAndComments.vue';
import FilesUploader from '@/components/candidacy/FilesUploader.vue';
import type { Job } from '@/stores/job/schema';
import CandidacyHeader from '@/components/candidacy/CandidacyHeader.vue';
import CandidacyFooter from '@/components/candidacy/CandidacyFooter.vue';
import { createCandidate } from '@/stores/candidate/';
import type { Candidate } from '@/stores/candidate/schema';
import Success from '@/components/Success.vue';
import type { RawCandidacy } from '@/stores/candidacy/schema';
import type { NewCandidateRequest } from '@/stores/candidate/schema';

const jobId = ref<number>();
const toast = useToast();
const headerModalOpen = ref(false);
const job = ref<Job>();

const newCandidacyError = ref('');
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

// candidacy details
const candidacy = ref<RawCandidacy>({
  relevantExperience: 0,
  expectedCtc: 0,
  officialNoticePeriod: 0,
  actualNoticePeriod: 0,
  reasonForQuickJoin: '',
  recruiterComment: '',
});

const candidate = ref<NewCandidateRequest>({
  name: '',
  phone: '',
  email: '',
  pan: '',
  totalExperience: 0,
  education: '',
  currentCtc: 0,
});

// candidacy submission
const candidacySubmitted = ref(false);
const submittingNewCandidacy = ref(false);
// candidate
const searchingForCandidate = ref(false);
const selectedCandidate = ref<Candidate | null>();
const searchedCandidate = ref<Candidate>();
const creatingCandidate = ref(false);
const candidateCreated = ref(false);

async function submit(selectedCandidate: Candidate | null | undefined) {
  if (!selectedCandidate || !jobId.value) return;
  submittingNewCandidacy.value = true;
  try {
    await submitCandidacy({
      ...candidacy.value,
      jobId: jobId.value,
      candidatePan: selectedCandidate.pan,
    });
    candidacySubmitted.value = true;
  } catch (err) {
    if (err instanceof ApiError) newCandidacyError.value = err.message;
  } finally {
    submittingNewCandidacy.value = false;
  }
}

async function createNewCandidate(candidate: NewCandidateRequest) {
  creatingCandidate.value = true;
  try {
    const res = await createCandidate(candidate);
    searchedCandidate.value = res.candidate;
    candidateCreated.value = true;
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
  } finally {
    creatingCandidate.value = false;
  }
}

async function searchCandidate(identifier: string) {
  searchingForCandidate.value = true;
  try {
    const res = await findCandidate(identifier);
    searchedCandidate.value = res.candidate;
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
  } finally {
    searchingForCandidate.value = false;
  }
}

function selectCandidate(
  candidate: Candidate | NewCandidateRequest | null,
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
