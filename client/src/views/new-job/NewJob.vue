<template>
  <Toast />
  <div class="flex w-full flex-col gap-8 pb-6">
    <div v-if="!jobCreated" class="flex h-full w-full flex-col gap-6">
      <JobInformation :disabled="false" :jobDetails="job" @input="(details) => (job = details)" />
      <JobHiringDetails @input="(details) => (job = details)" :disabled="false" :jobDetails="job" />
      <NowJobPaymentDetails
        :disabled="false"
        :jobDetails="job"
        @input="(details) => (job = details)"
      />
      <div class="space-y-3">
        <label>Skills & Questions</label>
        <SkillsDropdown :disabled="false" @addSkill="(skill: Skill) => addSkill(skill)" />
        <Questions
          :disabled="false"
          :skills="job.skills"
          @removeSkill="(skill: Skill) => removeSkill(skill)"
        />
      </div>
    </div>
    <div v-else class="flex h-full w-full items-center justify-center">
      <Success :message="'Job created successfully!'" />
    </div>

    <JobFooter
      :visible="true"
      :saving="creatingJob"
      :saved="jobCreated"
      :isUpdate="false"
      @save="create()"
    />
  </div>
</template>

<script setup lang="ts">
import JobInformation from '@/components/job/shared/JobInformation.vue';
import JobHiringDetails from '@/components/job/shared/JobHiringDetails.vue';
import NowJobPaymentDetails from '@/components/job/shared/JobPaymentDetails.vue';
import Toast from 'primevue/toast';
import { useToast } from 'primevue/usetoast';
import { createJob } from '@/stores/job';
import type { NewJobRequest } from '@/stores/job/types';
import { ref } from 'vue';
import Success from '@/components/Success.vue';
import JobFooter from '@/components/job/shared/JobFooter.vue';
import type { Skill } from '../../stores/skill/types';

const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};
const jobCreated = ref(false);
const creatingJob = ref(false);

async function create() {
  creatingJob.value = true;
  try {
    await createJob(job.value);
    jobCreated.value = true;
  } catch (e) {
    showError(e as string);
  } finally {
    creatingJob.value = false;
  }
}

const removeSkill = (skill: Skill): void => {
  if (!job.value.skills.includes(skill)) return;
  job.value.skills.splice(job.value.skills.indexOf(skill), 1);
};

const addSkill = (skill: Skill): void => {
  if (job.value.skills.some((s) => s.name === skill.name)) return;
  job.value.skills.unshift(skill);
};

const job = ref<NewJobRequest>({
  client: '',
  name: '',
  status: 'OPEN',
  contractType: { contractTypeName: 'TEMPORARY' },
  wantedCvs: 0,
  experienceRangeMin: 0,
  experienceRangeMax: 0,
  noticePeriodInDays: 0,
  skills: [],
  salaryBudget: 0,
  currency: 'INR',
  description: '',
  bonusPayPerCv: 0,
  closureBonus: 0,
  closureBonusPaymentDate: new Date(),
  cvRatePaymentDate: new Date(),
});
</script>
