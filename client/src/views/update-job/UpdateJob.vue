<script setup lang="ts">
import JobInformation from '../../components/update-job/JobInformation.vue';
import HiringDetails from '../../components/update-job/HiringDetails.vue';
import Skills from '../../components/update-job/Skills.vue';
import { ref, onBeforeMount } from 'vue';
import { useRoute } from 'vue-router';
import { ApiError } from '../../utils/types';
import { useToast } from 'primevue/usetoast';
import Toast from 'primevue/toast';
import type { JobDto } from '../../stores/job/types';
import { getJobDetails, updateJob } from '@/stores/job';
import type { SkillDto } from '../../stores/skill/types';

const loading = ref(false);
const jobDetails = ref<JobDto>();
const route = useRoute();
const jobId = ref(route.params.jobId);

const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

function deleteSkill(skill: SkillDto) {
  if (jobDetails.value && jobDetails.value?.skills.length > -1) {
    jobDetails.value.skills.splice(jobDetails.value.skills.indexOf(skill), 1); // 2nd parameter means remove one item only
  }
}

async function loadJobData(jobId: number) {
  loading.value = true;
  try {
    return await getJobDetails(jobId);
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
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

onBeforeMount(async () => {
  loading.value = true;
  try {
    jobDetails.value = await loadJobData(Number(jobId.value));
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
  } finally {
    loading.value = false;
  }
});
</script>
<template>
  <Toast />
  <div class="flex w-full flex-col gap-8 pb-6">
    <div v-if="jobDetails" class="flex h-full w-full flex-col gap-6">
      <JobInformation :job-details="jobDetails" />
      <HiringDetails :job-details="jobDetails" />
      <Skills :skills="jobDetails.skills" @removeSkill="(skill) => deleteSkill(skill)" />
    </div>
    <div v-else>Spinner loading...</div>
    <div class="flex w-full justify-end">
      <Button label="Update Job" @click="update(jobDetails)"></Button>
    </div>
  </div>
</template>
