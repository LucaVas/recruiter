<script setup lang="ts">
import CandidateSection from '@/components/candidacy/candidate/CandidateSection.vue';
import { useToast } from 'primevue/usetoast';
import Toast from 'primevue/toast';
import Divider from 'primevue/divider';
import { onMounted, ref } from 'vue';
import { getJobDetails } from '@/stores/job';
import { useRoute, useRouter } from 'vue-router';
import { getSeverity, getStatusIcon, formatStatus } from '@/components/update-job/utils';
import type { JobStatus } from '@/stores/job/types';
import { submitCandidacy } from '@/stores/candidate';
import { ApiError } from '../../utils/types';
import HiringDetails from '@/components/candidacy/HiringDetails.vue';
import RemarksAndComments from '@/components/candidacy/RemarksAndComments.vue';
import FilesUploader from '@/components/candidacy/FilesUploader.vue';
import { formToNewCandidate } from './index';

const router = useRouter();
const route = useRoute();
const jobId = ref(route.params.jobId);
const toast = useToast();

const newCandidacyError = ref('');
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};
const showSuccess = (content: string) => {
  toast.add({ severity: 'success', summary: 'Success', detail: content, life: 3000 });
};
const jobDetails = ref({
  name: '',
  client: '',
  status: 'OPEN' as JobStatus,
});

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
const candidateSelectedId = ref<number | null>();
const candidateSubmitted = ref(false);
const submittingNewCandidate = ref(false);

async function submit() {
  if (!candidateSelectedId.value) return;
  submittingNewCandidate.value = true;
  const candidacy = formToNewCandidate(candidacyDetails.value, candidateSelectedId.value);
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
        @selectCandidate="(candidateId) => (candidateSelectedId = candidateId)"
        @passError="(e) => showError(e)"
      />

      <HiringDetails
        @input="
          (details) =>
            (candidacyDetails = {
              ...candidacyDetails,
              relevantExperience: details.relevantExperience,
              expectedCtc: details.expectedCtc,
              officialNoticePeriod: details.officialNoticePeriod,
              actualNoticePeriod: details.actualNoticePeriod,
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

    <div class="flex w-full justify-end">
      <Button
        v-if="jobDetails.status !== 'ARCHIVED' && !candidateSubmitted"
        label="Submit"
        size="small"
        @click="submit()"
        :loading="submittingNewCandidate"
      ></Button>
      <Button
        v-if="candidateSubmitted"
        label="Back to Dashboard"
        size="small"
        @click="router.push({ name: 'Dashboard' })"
      ></Button>
    </div>
  </div>
</template>
