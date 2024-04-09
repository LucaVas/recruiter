<script setup lang="ts">
import CandidateSection from '@/components/candidacy/candidate/CandidateSection.vue';
import { useToast } from 'primevue/usetoast';
import Toast from 'primevue/toast';
import Divider from 'primevue/divider';
import { onMounted, ref } from 'vue';
import { getJobDetails } from '@/stores/job';
import { useRoute } from 'vue-router';
import { getSeverity, getStatusIcon, formatStatus } from '@/components/update-job/utils';
import type { JobStatus } from '@/stores/job/types';
import { submitCandidacy } from '@/stores/candidate';
import { ApiError } from '../../utils/types';
import type { NewCandidacyDto } from '../../stores/candidate/types';
import HiringDetails from '@/components/candidacy/HiringDetails.vue';
import RemarksAndComments from '@/components/candidacy/RemarksAndComments.vue';
import FilesUploader from '@/components/candidacy/FilesUploader.vue';

const route = useRoute();
const jobId = ref(route.params.jobId);
const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};
const jobDetails = ref({
  name: '',
  client: '',
  status: 'OPEN' as JobStatus,
});

const newCandidacyError = ref('');
const candidateSelectedId = ref<number | null>();

// candidacy details
const candidacyDetails = ref({
  jobId: jobId,
  relevantExperience: '',
  expectedCtc: '',
  officialNoticePeriod: '',
  actualNoticePeriod: '',
  reasonForQuickJoin: '',
  remarks: '',
  comments: '',
});
type CandidacyDetails = typeof candidacyDetails.value;
function formToNewCandidate(candidacyDetails: CandidacyDetails): NewCandidacyDto {
  return {
    ...candidacyDetails,
    jobId: Number(candidacyDetails.jobId),
    candidateId: Number(candidateSelectedId.value),
    relevantExperience: Number(candidacyDetails.relevantExperience),
    expectedCtc: Number(candidacyDetails.expectedCtc),
    officialNoticePeriod: Number(candidacyDetails.officialNoticePeriod),
    actualNoticePeriod: Number(candidacyDetails.actualNoticePeriod),
  };
}

// candidacy submission
const candidateSubmitted = ref(false);
const submittingNewCandidate = ref(false);

async function submit() {
  submittingNewCandidate.value = true;
  const candidacy = formToNewCandidate(candidacyDetails.value);
  try {
    await submitCandidacy(candidacy);
    candidateSubmitted.value = true;
  } catch (err) {
    if (err instanceof ApiError) newCandidacyError.value = err.message;
  } finally {
    submittingNewCandidate.value = false;
  }
}

onMounted(async () => {
  const job = await getJobDetails(Number(jobId.value));
  jobDetails.value = {
    name: job.name,
    client: job.client,
    status: job.status,
  };
});
</script>
<template>
  <Toast />
  <div class="flex w-full flex-col gap-8 pb-6">
    <div class="flex h-full w-full flex-col gap-6">
      <!-- Job information -->
      <div class="flex w-full items-center gap-2">
        <Tag
          :icon="getStatusIcon(jobDetails.status)"
          :severity="getSeverity(jobDetails.status)"
          class="h-10 min-w-fit px-4"
          :value="formatStatus(jobDetails.status)"
        />
        <p>{{ jobDetails.name }} ({{ jobDetails.client }})</p>
      </div>
      <Divider />

      <CandidateSection
        @select-candidate="(candidate) => console.log(candidate)"
        @pass-error="(e) => showError(e)"
      />

      <HiringDetails :candidacy-details="candidacyDetails" :is-archived="false" />

      <RemarksAndComments :is-archived="false" />

      <FilesUploader />
    </div>

    <div class="flex w-full justify-end">
      <Button
        v-if="jobDetails.status !== 'ARCHIVED'"
        label="Submit"
        size="small"
        @click="submit()"
      ></Button>
    </div>
  </div>
</template>
