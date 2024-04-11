<template>
  <Toast />
  <div class="flex w-full flex-col gap-8 pb-6">
    <div class="flex h-full w-full flex-col gap-6">
      <JobInformation
        :disabled="false"
        :jobDetails="job"
        @input="(details) => updateNewJobInformation(details)"
      />
      <JobHiringDetails
        @input="(details) => updateNewJobHiringDetails(details)"
        :disabled="false"
        :jobDetails="job"
      />
      <NowJobPaymentDetails
        :disabled="false"
        :jobDetails="job"
        @input="(details) => updateNewJobPaymentDetails(details)"
      />
      <Skills
        :disabled="false"
        :jobSkills="job.skills"
        @updateSkills="(skills) => updateNewJobSkills(skills)"
      />
    </div>
    <div class="flex w-full justify-end">
      <Button label="Create Job" @click="create()" />
    </div>
  </div>
</template>

<script setup lang="ts">
import JobInformation from '@/components/job/shared/JobInformation.vue';
import JobHiringDetails from '@/components/job/shared/JobHiringDetails.vue';
import NowJobPaymentDetails from '@/components/job/shared/JobPaymentDetails.vue';
import Skills from '@/components/job/shared/Skills.vue';
import Toast from 'primevue/toast';
import { useToast } from 'primevue/usetoast';
import {
  updateNewJobInformation,
  updateNewJobHiringDetails,
  job,
  updateNewJobPaymentDetails,
  updateNewJobSkills,
} from './index';
import { createJob } from '@/stores/job';

const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

async function create() {
  try {
    await createJob(job.value);
  } catch (e) {
    showError(e as string);
  }
}
</script>
