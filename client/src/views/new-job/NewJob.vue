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
        <label>Skills</label>
        <SkillsDropdown :disabled="false" @addSkill="(skill: Skill) => addSkill(skill)" />
        <JobSkills
          :isNewJob="true"
          @remove="(skill: Skill) => removeSkill(skill)"
          :skills="job.skills"
        />
      </div>
      <div class="space-y-3">
        <label>Questions</label>
        <QuestionsSearch
          @createNewQuestion="openQuestionModal = true"
          @searchQuestions="(clientOrSkill) => search(clientOrSkill)"
        />
        <QuestionsTable :questions="questions" />
        <QuestionModal
          :visible="openQuestionModal"
          :isUpdate="false"
          @close="openQuestionModal = false"
          @save="
            (question) => {
              console.log(question);
              openQuestionModal = false;
            }
          "
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
import JobSkills from '@/components/job/job-page/JobSkills.vue';
import QuestionsSearch from '@/components/question/QuestionSearch.vue';
import Toast from 'primevue/toast';
import { useToast } from 'primevue/usetoast';
import { createJob } from '@/stores/job';
import type { NewJobRequest } from '@/stores/job/schema';
import { ref } from 'vue';
import Success from '@/components/Success.vue';
import JobFooter from '@/components/job/shared/JobFooter.vue';
import type { Skill } from '@/stores/skill/schema';
import type { Client } from '@/stores/client/schema';
import QuestionModal from '@/components/question/QuestionModal.vue';
import { searchQuestions } from '@/stores/question';
import { type Question } from '@/stores/question/schema';
import QuestionsTable from '@/components/question/QuestionsTable.vue';

const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};
const jobCreated = ref(false);
const creatingJob = ref(false);
const openQuestionModal = ref(false);
const searchingQuestions = ref(false);
const questions = ref<Question[]>();

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

async function search(clientOrSkill: string) {
  searchingQuestions.value = true;
  try {
    questions.value = await searchQuestions(clientOrSkill);
  } catch (e) {
    showError(e as string);
  } finally {
    searchingQuestions.value = false;
  }
}

const removeSkill = (skill: Skill): void => {
  if (!job.value.skills.includes(skill)) return;
  job.value.skills.splice(job.value.skills.indexOf(skill), 1);
};

const addSkill = (skill: Skill): void => {
  if (job.value.skills.some((s: Skill) => s.name === skill.name)) return;
  job.value.skills.unshift(skill);
};

const job = ref<NewJobRequest>({
  client: {} as Client,
  name: '',
  status: 'OPEN',
  contractType: 'TEMPORARY',
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
