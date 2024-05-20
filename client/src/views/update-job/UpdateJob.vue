<template>
  <div v-if="!jobDetails" class="flex w-full items-center justify-center">
    <ProgressSpinner />
  </div>
  <div v-else class="flex w-full flex-col gap-8 pb-6">
    <div v-if="!jobUpdated" class="flex h-full w-full flex-col gap-6">
      <JobStatusComponent
        :status="jobDetails.status"
        :createdAt="jobDetails.createdDTime"
        @delete="delJob(jobDetails.id)"
        @changeStatus="(status: JobStatus) => changeStatus(jobDetails!.id, status)"
      />
      <JobInformation
        :disabled="jobDetails.status === 'DELETED'"
        :jobDetails="jobDetails"
        :clients="clients"
      />
      <JobHiringDetails :disabled="jobDetails.status === 'DELETED'" :jobDetails="jobDetails" />
      <JobPaymentDetails :disabled="jobDetails.status === 'DELETED'" :jobDetails="jobDetails" />
      <Skills
        :disabled="jobDetails.status === 'DELETED'"
        :skills="jobDetails.skills && skills"
        @update="(skills) => (jobDetails!.skills = skills)"
      />
    </div>
    <Success v-else :message="'Job updated successfully!'" />
    <JobFooter
      :saving="updatingJob"
      :saved="jobUpdated"
      :isUpdate="true"
      :visible="jobDetails.status !== 'DELETED'"
      @save="update(jobDetails!)"
    />
  </div>
</template>

<script setup lang="ts">
import JobInformation from '@/components/job/shared/JobInformation.vue';
import JobHiringDetails from '@/components/job/shared/JobHiringDetails.vue';
import JobPaymentDetails from '@/components/job/shared/JobPaymentDetails.vue';
import Skills from '@/components/job/shared/Skills.vue';
import JobFooter from '@/components/job/shared/JobFooter.vue';
import JobStatusComponent from '@/components/job/update-job/JobStatusComponent.vue';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ProgressSpinner from 'primevue/progressspinner';
import type { Job, JobStatus } from '@/stores/job/schema';
import { useToast } from 'primevue/usetoast';
import { getJob, updateJob, deleteJob, changeJobStatus } from '@/stores/job';
import { ApiError } from '@/utils/types';
import Success from '@/components/Success.vue';
import { getAllClients } from '@/stores/client';
import { showError } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import {
  loading,
  updatingJob,
  jobUpdated,
  jobDetails,
  clients,
  changingStatus,
  deletingJob,
  skills,
} from './index';
import { showSuccess } from '../../utils/errorUtils';
import { getAllSkills } from '@/stores/skill';

// variables
const route = useRoute();
const jobId = ref(route.params.id);
const router = useRouter();
const toast = useToast();

// functions
async function loadJobData(jobId: number) {
  loading.value = true;
  try {
    const job = await getJob(jobId);
    return job;
  } catch (err) {
    if (err instanceof ApiError) {
      if (err.statusCode === 401) router.push({ name: 'Dashboard' });
      else showError(toast, err.message, err.title);
    } else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    loading.value = false;
  }
}

async function update(job: Job) {
  updatingJob.value = true;
  try {
    await updateJob(job);
    jobUpdated.value = true;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    updatingJob.value = false;
  }
}

async function changeStatus(id: number, status: JobStatus) {
  changingStatus.value = true;
  try {
    await changeJobStatus(id, status);
    showSuccess(toast, 'Job status changed successfully.');
    await Promise.all([
      (jobDetails.value = await loadJobData(Number(jobId.value))),
      (clients.value = await getAllClients()),
    ]);
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    changingStatus.value = false;
  }
}

async function delJob(id: number) {
  deletingJob.value = true;
  try {
    await deleteJob(id);
    router.push({ name: 'Dashboard' });
    setTimeout(() => {
      showSuccess(toast, 'Job deleted successfully.');
    }, 1000);
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    deletingJob.value = false;
  }
}

const loadSkills = async () => {
  try {
    skills.value = await getAllSkills();
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  }
};

// init
onMounted(async () => {
  await Promise.all([
    (jobDetails.value = await loadJobData(Number(jobId.value))),
    (clients.value = await getAllClients()),
    loadSkills(),
  ]);
});
</script>
