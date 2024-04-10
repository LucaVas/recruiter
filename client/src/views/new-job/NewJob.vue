<script setup lang="ts">
import NewJobInformation from '@/components/new-job/NewJobInformation.vue';
import NewJobHiringDetails from '@/components/new-job/NewJobHiringDetails.vue';
import NowJobPaymentDetails from '@/components/new-job/NewJobPaymentDetails.vue';
import Toast from 'primevue/toast';
import { useToast } from 'primevue/usetoast';
import {
  updateNewJobInformation,
  updateNewJobHiringDetails,
  job,
  updateNewJobPaymentDetails,
} from './index';
import { createJob } from '@/stores/job';

const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

async function create() {
  try {
    console.log(job.value)
    await createJob(job.value);
  } catch (e) {
    showError(e as string);
  }
}
</script>
<template>
  <Toast />
  <div class="flex w-full flex-col gap-8 pb-6">
    <div class="flex h-full w-full flex-col gap-6">
      <NewJobInformation @input="(details) => updateNewJobInformation(details)" />
      <NewJobHiringDetails @input="(details) => updateNewJobHiringDetails(details)" />
      <NowJobPaymentDetails @input="(details) => updateNewJobPaymentDetails(details)" />
      <!-- <NewJobSkills
        :skills="job.skills"
        @removeSkill="(skill) => deleteSkill(skill)"
      /> -->
    </div>
    <div class="flex w-full justify-end">
      <Button label="Create Job" @click="create()" />
    </div>
  </div>
</template>
