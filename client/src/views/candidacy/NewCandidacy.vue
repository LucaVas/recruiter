<script setup lang="ts">
import CandidateSection from '@/components/candidacy/CandidateSection.vue';
import CandidacyHiringDetailsModal from '@/components/candidacy/header/CandidacyHiringDetailsModal.vue';
import { useToast } from 'primevue/usetoast';
import Toast from 'primevue/toast';
import { onMounted, ref } from 'vue';
import { getJob } from '@/stores/job';
import { useRoute, useRouter } from 'vue-router';
import { submitCandidacy } from '@/stores/candidate';
import { ApiError } from '../../utils/types';
import HiringDetails from '@/components/candidacy/HiringDetails.vue';
import RemarksAndComments from '@/components/candidacy/RemarksAndComments.vue';
import FilesUploader from '@/components/candidacy/FilesUploader.vue';
import { formToNewCandidate } from './index';
import type { JobDto } from '../../stores/job/types';
import Header from '@/components/candidacy/Header.vue';

const router = useRouter();
const jobId = ref<number>();
const toast = useToast();
const headerModalOpen = ref(false);

const newCandidacyError = ref('');
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};
const showSuccess = (content: string) => {
  toast.add({ severity: 'success', summary: 'Success', detail: content, life: 3000 });
};
const job = ref<JobDto>();

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
  const route = useRoute();
  jobId.value = Number(route.params.id);
  job.value = await getJob(jobId.value);
});
</script>
<template>
  <Toast />
  <div class="flex w-full flex-col gap-8 pb-6">
    <div class="flex h-full w-full flex-col gap-6">
      <div v-if="job">
        <Header
          :status="job.status"
          :client="job.client"
          :name="job.name"
          @openModal="headerModalOpen = true"
        />
        <CandidacyHiringDetailsModal
          :visible="headerModalOpen"
          @close="headerModalOpen = false"
          :contractType="job.contractType.contractTypeName"
          :wantedCvs="job.wantedCvs"
          :candidates="job.numberOfCandidates"
          :experienceRangeMin="job.experienceRangeMin"
          :experienceRangeMax="job.experienceRangeMax"
          :noticePeriodInDays="job.noticePeriodInDays"
          :salaryBudget="job.salaryBudget"
          :currency="job.currency"
          :bonusPayPerCv="job.bonusPayPerCv"
          :closureBonus="job.closureBonus"
          :closureBonusPaymentDate="job.closureBonusPaymentDate"
          :cvRatePaymentDate="job.cvRatePaymentDate"
        />
      </div>

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
        v-if="job?.status !== 'ARCHIVED' && !candidateSubmitted"
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
