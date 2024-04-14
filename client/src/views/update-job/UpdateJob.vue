<template>
  <Toast />
  <div v-if="!jobDetails" class="flex w-full items-center justify-center">
    <ProgressSpinner />
  </div>
  <div v-else class="flex w-full flex-col gap-8 pb-6">
    <div v-if="!jobUpdated" class="flex h-full w-full flex-col gap-6">
      <JobStatus
        :status="jobDetails.status"
        :createdAt="jobDetails.createdAt"
        @delete="delJob(jobDetails.id)"
        @changeStatus="(status: JobStatus) => changeStatus(jobDetails!.id, status)"
      />
      <JobInformation :disabled="jobDetails.status === 'ARCHIVED'" :jobDetails="jobDetails" />
      <JobHiringDetails :disabled="jobDetails.status === 'ARCHIVED'" :jobDetails="jobDetails" />
      <JobPaymentDetails :disabled="jobDetails.status === 'ARCHIVED'" :jobDetails="jobDetails" />
      <Skills
        :disabled="jobDetails.status === 'ARCHIVED'"
        :skills="jobDetails.skills"
        @update="(skills) => (jobDetails!.skills = skills)"
      />
    </div>
    <Success v-else :message="'Job updated successfully!'" />
    <JobFooter
      :saving="updatingJob"
      :saved="jobUpdated"
      :isUpdate="true"
      :visible="jobDetails.status !== 'ARCHIVED'"
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
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Toast from 'primevue/toast';
import ProgressSpinner from 'primevue/progressspinner';
import type { Job, JobStatus } from '@/stores/job/types';
import { useToast } from 'primevue/usetoast';
import { getJob, updateJob, deleteJob, changeJobStatus } from '@/stores/job';
import { ApiError } from '@/utils/types';
import Success from '@/components/Success.vue';

// variables
const jobDetails = ref<Job>();
const route = useRoute();
const jobId = ref(route.params.id);
const router = useRouter();
const loading = ref(false);
const updatingJob = ref(false);
const jobUpdated = ref(false);
const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

// functions
async function loadJobData(jobId: number) {
  loading.value = true;
  try {
    const job = await getJob(jobId);
    return job;
  } catch (err) {
    if (err instanceof ApiError) {
      if (err.statusCode === 401) router.push({ name: 'Dashboard' });
      else showError(err.message);
    }
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
    if (err instanceof ApiError) showError(err.message);
  } finally {
    updatingJob.value = false;
  }
}

async function changeStatus(id: number, status: JobStatus) {
  updatingJob.value = true;
  try {
    await changeJobStatus(id, status);
    jobUpdated.value = true;
    router.go(0);
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
  } finally {
    updatingJob.value = false;
  }
}

async function delJob(id: number) {
  try {
    await deleteJob(id);
    router.go(0);
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
  }
}

// init
onMounted(async () => {
  jobDetails.value = await loadJobData(Number(jobId.value));
});
</script>
