<template>
  <div class="flex w-full flex-col gap-8 pb-6">
    <div v-if="!jobCreated" class="flex h-full w-full flex-col gap-6">
      <JobInformation
        :disabled="false"
        :jobDetails="job"
        :clients="clients"
        :selectedClient="job.client"
        @input="(details) => (job = details)"
        @selectClient="
          (client) => {
            job.client = client;
            clients.push(client);
          }
        "
      />
      <JobHiringDetails @input="(details) => (job = details)" :disabled="false" :jobDetails="job" />
      <NowJobPaymentDetails
        :disabled="false"
        :jobDetails="job"
        @input="(details) => (job = details)"
      />
      <div class="space-y-3">
        <NewSkillModal
          :visible="openSkillModal"
          :creatingSkill="creatingSkill"
          @close="openSkillModal = false"
          @save="(skill: NewSkill) => createNewSkill(skill)"
        />
        <label>Skills</label>
        <div class="flex gap-3">
          <SkillsDropdown
            :skills="skills"
            :disabled="false"
            class="w-full"
            @addSkill="(skill: Skill) => addSkill(job, skill)"
          />
          <Button
            label="New"
            icon="pi pi-plus"
            @click="openSkillModal = true"
            class="hidden min-w-fit md:block"
          />
          <Button icon="pi pi-plus" @click="openSkillModal = true" class="min-w-fit md:hidden" />
        </div>

        <JobSkills
          :isNewJob="true"
          @remove="(skill: Skill) => removeSkill(job, skill)"
          :skills="job.skills"
        />
      </div>
      <div class="space-y-3">
        <QuestionModal
          :clients="clients"
          :visible="openQuestionModal"
          :isUpdate="false"
          @close="openQuestionModal = false"
          @save="
            (question) => {
              createAndAddQuestion(question);
              openQuestionModal = false;
            }
          "
        />
        <QuestionSearchModal
          :questionsSelected="job.questions"
          :visible="openQuestionSearchModal"
          @close="openQuestionSearchModal = false"
          @selectOrUnselectQuestions="
            (questions) => {
              job.questions.push(...questions);
              openQuestionSearchModal = false;
            }
          "
        />
        <label>Questions</label>

        <div class="flex flex-row gap-3">
          <IconField iconPosition="left" class="w-full">
            <InputIcon class="pi pi-search"> </InputIcon>
            <InputText
              placeholder="Search"
              class="w-full"
              @click="openQuestionSearchModal = true"
            />
          </IconField>

          <Button
            label="New"
            icon="pi pi-plus"
            @click="openQuestionModal = true"
            class="hidden min-w-fit md:block"
          />
          <Button icon="pi pi-plus" @click="openQuestionModal = true" class="min-w-fit md:hidden" />
        </div>
        <QuestionsTable
          :visible="job.questions.length > 0"
          :questions="job.questions"
          @removeQuestion="(question) => job.questions?.splice(job.questions.indexOf(question), 1)"
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
      @save="create(job)"
    />
  </div>
</template>

<script setup lang="ts">
import JobInformation from '@/components/job/shared/JobInformation.vue';
import SkillsDropdown from '@/components/job/shared/SkillsDropdown.vue';
import JobHiringDetails from '@/components/job/shared/JobHiringDetails.vue';
import NowJobPaymentDetails from '@/components/job/shared/JobPaymentDetails.vue';
import JobSkills from '@/components/job/job-page/JobSkills.vue';
import QuestionSearchModal from '@/components/question/QuestionSearchModal.vue';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import { useToast } from 'primevue/usetoast';
import { createJob } from '@/stores/job';
import type { NewJobRequest } from '@/stores/job/schema';
import { onMounted } from 'vue';
import Success from '@/components/Success.vue';
import JobFooter from '@/components/job/shared/JobFooter.vue';
import type { NewSkill, Skill } from '@/stores/skill/schema';
import QuestionModal from '@/components/question/QuestionModal.vue';
import QuestionsTable from '@/components/question/QuestionsTable.vue';
import { createQuestion } from '@/stores/question/index';
import { type QuestionForm } from '@/stores/question/schema';
import { capitalize, capitalizeText, capitalizeWords } from '../../utils/stringUtils';
import { getAllClients } from '@/stores/client';
import { getAllSkills } from '@/stores/skill';
import { showError } from '@/utils/errorUtils';
import { ApiError } from '@/utils/types';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import {
  job,
  creatingJob,
  jobCreated,
  clients,
  skills,
  addSkill,
  removeSkill,
  openQuestionModal,
  openSkillModal,
  openQuestionSearchModal,
  creatingSkill,
  skillCreated,
} from './index';
import type NewSkillModal from '@/components/skill/NewSkillModal.vue';
import { createSkill } from '../../stores/skill/index';

const toast = useToast();

async function create(job: NewJobRequest) {
  creatingJob.value = true;
  try {
    await createJob(job);
    jobCreated.value = true;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    creatingJob.value = false;
  }
}

const createNewSkill = async (skill: NewSkill) => {
  creatingSkill.value = true;
  try {
    await createSkill(skill);
    skillCreated.value = true;
    openSkillModal.value = false;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    creatingSkill.value = false;
  }
};

const loadClients = async () => {
  try {
    clients.value = await getAllClients();
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  }
};

const loadSkills = async () => {
  try {
    skills.value = await getAllSkills();
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  }
};

const createAndAddQuestion = async (question: QuestionForm): Promise<void> => {
  try {
    const newQuestion = await createQuestion({
      ...question,
      text: capitalizeText(question.text),
      title: capitalizeWords(question.title),
      answer: capitalizeText(question.answer),
      division: capitalize(question.division),
      skillNames: question.skillNames.map((s) => capitalize(s)),
    });
    job.value.questions.push(newQuestion);
    skills.value = await getAllSkills();
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  }
};

onMounted(async () => {
  await Promise.all([loadClients(), loadSkills()]);
});
</script>
