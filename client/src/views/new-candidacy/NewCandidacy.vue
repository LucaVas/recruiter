<script setup lang="ts">
import CandidateSection from '@/components/candidacy/CandidateSection.vue';
import CandidacyHiringDetailsModal from '@/components/candidacy/header/CandidacyHiringDetailsModal.vue';
import { useToast } from 'primevue/usetoast';
import Toast from 'primevue/toast';
import { onMounted, ref } from 'vue';
import { getJob } from '@/stores/job';
import { useRoute } from 'vue-router';
import { findCandidate } from '@/stores/candidate';
import { submitCandidacy } from '@/stores/candidacy';
import { ApiError } from '../../utils/types';
import HiringDetails from '@/components/candidacy/HiringDetails.vue';
import RemarksAndComments from '@/components/candidacy/RemarksAndComments.vue';
import FilesUploader from '@/components/candidacy/FilesUploader.vue';
import type { CandidateForm } from './index';
import type { JobDto } from '../../stores/job/types';
import CandidacyHeader from '@/components/candidacy/CandidacyHeader.vue';
import CandidacyFooter from '@/components/candidacy/CandidacyFooter.vue';
import { mapFormToNewCandidate } from '@/components/candidacy/candidate/shared/mappers';
import { createCandidate } from '@/stores/candidate/';
import type { RawCandidateDto } from '../../stores/candidate/types';
import { generateEmptyCandidateForm } from './utils';
import { formToNewCandidacy } from './index';
import CandidacySuccess from '@/components/candidacy/CandidacySuccess.vue';

const jobId = ref<number>();
const toast = useToast();
const headerModalOpen = ref(false);
const creatingCandidate = ref(false);
const job = ref<JobDto>();

const newCandidacyError = ref('');
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};
const showSuccess = (content: string) => {
  toast.add({ severity: 'success', summary: 'Success', detail: content, life: 3000 });
};

// candidacy details
const candidacyDetails = ref({
  jobId: jobId,
  relevantExperience: 0,
  expectedCtc: 0,
  officialNoticePeriod: 0,
  actualNoticePeriod: 0,
  reasonForQuickJoin: '',
  remarks: '',
  comments: '',
});

// candidacy submission
const candidateSubmitted = ref(false);
const submittingNewCandidate = ref(false);
const searchingForCandidate = ref(false);
const selectedCandidate = ref<(RawCandidateDto & { id: number }) | null>();
const searchedCandidate = ref<RawCandidateDto & { id: number }>();
const candidate = ref<CandidateForm>();

async function submit(selectedCandidate: (RawCandidateDto & { id: number }) | null | undefined) {
  if (!selectedCandidate) return;
  submittingNewCandidate.value = true;
  const candidacy = formToNewCandidacy(candidacyDetails.value, selectedCandidate.id);
  try {
    await submitCandidacy(candidacy);
    candidateSubmitted.value = true;
    showSuccess('Candidacy submitted!');
  } catch (err) {
    if (err instanceof ApiError) newCandidacyError.value = err.message;
  } finally {
    submittingNewCandidate.value = false;
  }
}

async function createNewCandidate(candidateForm: CandidateForm) {
  creatingCandidate.value = true;
  try {
    const newCandidate = mapFormToNewCandidate(candidateForm);
    const res = await createCandidate(newCandidate);
    searchedCandidate.value = res.candidate;
    candidate.value = generateEmptyCandidateForm();
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
    searchedCandidate.value = { ...res.candidate, id: res.id };
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
  } finally {
    searchingForCandidate.value = false;
  }
}

function selectCandidate(
  candidate: RawCandidateDto | null,
  searchedCandidate: (RawCandidateDto & { id: number }) | undefined
): void {
  if (!candidate || !searchedCandidate) return;
  if (candidate.pan === searchedCandidate.pan) selectedCandidate.value = searchedCandidate;
}

onMounted(async () => {
  const route = useRoute();
  jobId.value = Number(route.params.id);
  job.value = await getJob(jobId.value);
  candidate.value = generateEmptyCandidateForm();
});
</script>
<template>
  <Toast />
  <div class="flex w-full flex-col gap-8 pb-6">
    <div v-if="!candidateSubmitted" class="flex h-full w-full flex-col gap-6">
      <div v-if="job">
        <CandidacyHeader
          :status="job.status"
          :client="job.client"
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
        v-if="candidate"
        :searching="searchingForCandidate"
        :candidate="candidate"
        :searchedCandidate="searchedCandidate"
        @update="(candidateForm) => createNewCandidate(candidateForm)"
        @searchCandidate="(identifier) => searchCandidate(identifier)"
        @selectCandidate="(candidate) => selectCandidate(candidate, searchedCandidate)"
      />

      <HiringDetails
        :candidacy="candidacyDetails"
        @input="
          (details) =>
            (candidacyDetails = {
              ...candidacyDetails,
              relevantExperience: details.relevantExperience,
              expectedCtc: details.expectedCtc,
              officialNoticePeriod: details.officialNoticePeriod,
              actualNoticePeriod: Number(details.actualNoticePeriod),
              reasonForQuickJoin: details.reasonForQuickJoin,
            })
        "
        :is-archived="false"
      />

      <RemarksAndComments
        @input="
          (details) =>
            (candidacyDetails = {
              ...candidacyDetails,
              remarks: details.remarks,
              comments: details.comments,
            })
        "
        :is-archived="false"
      />

      <FilesUploader />
    </div>

    <div v-else class="flex h-full w-full items-center justify-center">
      <CandidacySuccess :message="'Candidacy submitted successfully!'" />
    </div>

    <CandidacyFooter
      v-if="job"
      :disabled="job.status === 'ARCHIVED'"
      :candidacySubmitted="candidateSubmitted"
      :submittingNewCandidacy="submittingNewCandidate"
      :isUpdate="false"
      @submit="submit(selectedCandidate)"
    />
  </div>
</template>
