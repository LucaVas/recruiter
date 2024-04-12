<script setup lang="ts">
import CandidateTable from '@/components/candidacy/candidate/CandidateTable.vue';
import CandidacyHiringDetailsModal from '@/components/candidacy/header/CandidacyHiringDetailsModal.vue';
import { useToast } from 'primevue/usetoast';
import Toast from 'primevue/toast';
import { onMounted, ref } from 'vue';
import { getCandidacy, updateCandidacy } from '@/stores/candidacy/index';
import { useRoute } from 'vue-router';
import HiringDetails from '@/components/candidacy/HiringDetails.vue';
import RemarksAndComments from '@/components/candidacy/RemarksAndComments.vue';
import FilesUploader from '@/components/candidacy/FilesUploader.vue';
import type { CandidateForm } from './index';
import type { JobDto } from '../../stores/job/types';
import CandidacyHeader from '@/components/candidacy/CandidacyHeader.vue';
import CandidacyFooter from '@/components/candidacy/CandidacyFooter.vue';
import { mapCandidateToForm } from '@/components/candidacy/candidate/shared/mappers';
import CandidacySuccess from '@/components/candidacy/CandidacySuccess.vue';
import { mapCandidacyToForm, type CandidacyForm, mapFormToCandidacy } from './mappers';
import { ApiError } from '../../utils/types';

const toast = useToast();
const headerModalOpen = ref(false);
const job = ref<JobDto>();

const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

// candidacy details
const candidacy = ref<CandidacyForm>();

// candidacy submission
const candidacyUpdated = ref(false);
const updatingCandidacy = ref(false);
const candidate = ref<CandidateForm>();

async function update(candidacyForm: CandidacyForm | undefined) {
  if (!candidacyForm) return;
  updatingCandidacy.value = true;
  const candidacy = mapFormToCandidacy(candidacyForm);
  try {
    await updateCandidacy(candidacy);
    candidacyUpdated.value = true;
  } catch (err) {
    if (err instanceof ApiError) showError(err.message)
  } finally {
    updatingCandidacy.value = false;
  }
}

onMounted(async () => {
  const route = useRoute();
  const candidacyId = Number(route.params.id);
  const res = await getCandidacy(candidacyId);
  candidate.value = mapCandidateToForm(res.candidacy.candidate);
  candidacy.value = mapCandidacyToForm(res.candidacy);
  job.value = res.candidacy.job;
});
</script>
<template>
  <Toast />
  <div class="flex w-full flex-col gap-8 pb-6">
    <div v-if="!candidacyUpdated" class="flex h-full w-full flex-col gap-6">
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

      <CandidateTable v-if="candidate" :candidateToDisplay="candidate" @selectCandidate="null" />

      <HiringDetails
        v-if="candidacy"
        :candidacy="candidacy"
        @input="
          (details) =>
            (candidacy = {
              ...candidacy,
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
            (candidacy = {
              ...candidacy,
              remarks: details.remarks,
              comments: details.comments,
            })
        "
        :is-archived="false"
      />

      <FilesUploader />
    </div>

    <div v-else class="flex h-full w-full items-center justify-center">
      <CandidacySuccess :message="'Candidacy updated!'" />
    </div>

    <CandidacyFooter
      v-if="job"
      :disabled="job.status === 'ARCHIVED'"
      :candidacySubmitted="candidacyUpdated"
      :submittingNewCandidacy="updatingCandidacy"
      :isUpdate="true"
      @update="update(candidacy)"
    />
  </div>
</template>
