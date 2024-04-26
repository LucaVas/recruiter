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
import type { Job } from '@/stores/job/schema';
import CandidacyHeader from '@/components/candidacy/CandidacyHeader.vue';
import CandidacyFooter from '@/components/candidacy/CandidacyFooter.vue';
import Success from '@/components/Success.vue';
import { ApiError } from '@/utils/types';
import type { Candidate } from '@/stores/candidate/schema';
import type { RawCandidacy } from '@/stores/candidacy/schema';
import type { UpdateCandidacyRequest } from '@/stores/candidacy/schema';

const toast = useToast();
const headerModalOpen = ref(false);
const job = ref<Job>();

const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

// candidacy details
const candidacy = ref<RawCandidacy>();
const jobId = ref<number>();
const pan = ref<string>();

// candidacy submission
const candidacyUpdated = ref(false);
const updatingCandidacy = ref(false);
const candidate = ref<Candidate>();

async function update(candidacy: UpdateCandidacyRequest | undefined) {
  if (!candidacy || !jobId.value || pan.value === undefined) return;
  updatingCandidacy.value = true;
  try {
    await updateCandidacy(jobId.value, pan.value, candidacy);
    candidacyUpdated.value = true;
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
  } finally {
    updatingCandidacy.value = false;
  }
}

onMounted(async () => {
  const route = useRoute();
  jobId.value = Number(route.params.jobId);
  pan.value = route.params.pan as string;
  const res = await getCandidacy(jobId.value, pan.value);
  candidate.value = res.candidacy.candidate;
  candidacy.value = res.candidacy;
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

      <FilesUploader />
    </div>

    <Success v-else :message="'Candidacy updated successfully!'" />

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
@/stores/job/schema @/stores/candidate/schema @/stores/candidacy/schema@/stores/candidacy/schema
