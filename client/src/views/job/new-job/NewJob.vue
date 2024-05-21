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
import { onMounted } from 'vue';
import Success from '@/components/Success.vue';
import JobFooter from '@/components/job/shared/JobFooter.vue';
import type { NewSkill, Skill } from '@/stores/skill/schema';
import QuestionModal from '@/components/question/QuestionModal.vue';
import QuestionsTable from '@/components/question/QuestionsTable.vue';
import {
  job,
  create,
  creatingJob,
  jobCreated,
  clients,
  loadClients,
  skills,
  loadSkills,
  addSkill,
  removeSkill,
  openQuestionModal,
  openQuestionSearchModal,
  createAndAddQuestion,
} from './index';
import type NewSkillModal from '@/components/skill/NewSkillModal.vue';
import { createNewSkill, creatingSkill, skillModalOpen } from '../jobCommons';

const toast = useToast();

onMounted(async () => {
  await Promise.all([loadClients(toast), loadSkills(toast)]);
});
</script>

<template>
  <div class="flex w-full flex-col gap-8 pb-6">
    <div v-if="!jobCreated" class="flex h-full w-full flex-col gap-6">
      <JobInformation
        :disabled="false"
        :job="job"
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
      <JobHiringDetails @input="(details) => (job = details)" :disabled="false" :job="job" />
      <NowJobPaymentDetails :disabled="false" :job="job" @input="(details) => (job = details)" />
      <div class="space-y-3">
        <NewSkillModal
          :visible="skillModalOpen"
          :creatingSkill="creatingSkill"
          @close="skillModalOpen = false"
          @save="
            async (newSkill: NewSkill) => {
              const skill = await createNewSkill(newSkill, toast);
              addSkill(job, skill);
            }
          "
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
            @click="skillModalOpen = true"
            class="hidden min-w-fit md:block"
          />
          <Button icon="pi pi-plus" @click="skillModalOpen = true" class="min-w-fit md:hidden" />
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
              createAndAddQuestion(question, toast);
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
      @save="create(job, toast)"
    />
  </div>
</template>
