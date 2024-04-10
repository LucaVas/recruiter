<script setup lang="ts">
import JobInformation from '@/components/update-job/JobInformation.vue';
import HiringDetails from '@/components/update-job/HiringDetails.vue';
import Skills from '@/components/update-job/Skills.vue';
import JobStatus from '@/components/update-job/JobStatus.vue';
import { ref, onBeforeMount } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Toast from 'primevue/toast';
import ProgressSpinner from 'primevue/progressspinner';
import type { JobDto } from '@/stores/job/types';
import type { SkillDto } from '@/stores/skill/types';
import { useToast } from 'primevue/usetoast';
import { getJob, updateJob } from '@/stores/job';
import { ApiError } from '@/utils/types';

const jobDetails = ref<JobDto>();
const route = useRoute();
const jobId = ref(route.params.jobId);
const router = useRouter();
const loading = ref(false);
const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

async function loadJobData(jobId: number) {
  loading.value = true;
  try {
    return await getJob(jobId);
  } catch (err) {
    if (err instanceof ApiError) {
      if (err.statusCode === 401) router.push({ name: 'Dashboard' });
      else showError(err.message);
    }
  } finally {
    loading.value = false;
  }
}

async function update(job: JobDto) {
  loading.value = true;
  try {
    await updateJob(job);
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
  } finally {
    loading.value = false;
  }
}

function deleteSkill(skill: SkillDto) {
  if (jobDetails.value && jobDetails.value?.skills.length > -1) {
    jobDetails.value.skills.splice(jobDetails.value.skills.indexOf(skill), 1); // 2nd parameter means remove one item only
  }
}

onBeforeMount(async () => {
  jobDetails.value = await loadJobData(Number(jobId.value));
});
</script>
<template>
  <Toast />
  <div class="flex w-full flex-col gap-8 pb-6">
    <div v-if="jobDetails" class="flex h-full w-full flex-col gap-6">
      <JobStatus
        :job-id="jobDetails.id"
        :status="jobDetails.status"
        :created-at="jobDetails.createdAt"
        @pass-error="(e) => showError(e)"
      />
      <JobInformation :is-archived="jobDetails.status === 'ARCHIVED'" :job-details="jobDetails" />
      <HiringDetails :is-archived="jobDetails.status === 'ARCHIVED'" :job-details="jobDetails" />
      <Skills
        :is-archived="jobDetails.status === 'ARCHIVED'"
        :skills="jobDetails.skills"
        @removeSkill="(skill) => deleteSkill(skill)"
      />
    </div>
    <div v-else class="flex w-full items-center justify-center">
      <ProgressSpinner />
    </div>
    <div class="flex w-full justify-end">
      <Button
        v-if="jobDetails?.status !== 'ARCHIVED'"
        label="Update Job"
        @click="update(jobDetails!)"
        
      ></Button>
    </div>
  </div>
</template>
