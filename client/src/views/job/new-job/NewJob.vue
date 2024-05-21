<script setup lang="ts">
import JobClientSection from '@/components/job/JobClientSection.vue';
import SkillsDropdown from '@/components/job/shared/SkillsDropdown.vue';
import JobSkills from '@/components/job/job-page/JobSkills.vue';
import QuestionSearchModal from '@/components/question/QuestionSearchModal.vue';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import { useToast } from 'primevue/usetoast';
import { onMounted } from 'vue';
import Success from '@/components/Success.vue';
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
import { useRouter } from 'vue-router';
import TextInput from '@/components/shared/TextInput.vue';
import NumberInput from '@/components/shared/NumberInput.vue';
import DateInput from '@/components/shared/DateInput.vue';
import TextArea from '@/components/shared/TextArea.vue';
import DropDown from '@/components/shared/DropDown.vue';
import { contractTypes, jobStatuses } from '@/components/job/shared/utils';

const toast = useToast();
const router = useRouter();

onMounted(async () => {
  await Promise.all([loadClients(toast), loadSkills(toast)]);
});
</script>

<template>
  <div class="flex w-full flex-col gap-8 pb-6">
    <div v-if="!jobCreated" class="flex h-full w-full flex-col gap-6">
      <JobClientSection
        :client="job.client"
        :clients="clients"
        @select="
          (client) => {
            job.client = client;
            clients.push(client);
          }
        "
      />

      <TextInput
        label="Job Name"
        :icon="'pi-briefcase'"
        :model="job.name"
        @input="(name) => (job.name = name)"
      />

      <div class="flex flex-col gap-6 sm:flex-row">
        <DropDown
          :model="job.status"
          label="Job Status"
          :options="jobStatuses"
          @select="(status) => (job.status = status)"
        />

        <DropDown
          :model="job.contractType"
          label="Contract Type"
          :options="contractTypes"
          @select="(contractType) => (job.contractType = contractType)"
        />
      </div>

      <div class="flex flex-col gap-6 sm:flex-row">
        <NumberInput
          label="Wanted CVs"
          icon="pi-file"
          :model="job.wantedCvs"
          :min="0"
          @input="(wantedCvs) => (job.wantedCvs = wantedCvs)"
        />

        <NumberInput
          label="Notice Period"
          icon="pi-calendar-times"
          :model="job.noticePeriodInDays"
          :min="0"
          trailing="days"
          @input="(noticePeriodInDays) => (job.noticePeriodInDays = noticePeriodInDays)"
        />
      </div>

      <div class="flex flex-col gap-2">
        <label class="text-sm">Experience range</label>
        <div class="flex flex-col gap-6 sm:flex-row">
          <NumberInput
            leading="From"
            trailing="Years"
            :min="0"
            :model="job.experienceRangeMin"
            @input="(experienceRangeMin) => (job.experienceRangeMin = experienceRangeMin)"
          />
          <NumberInput
            leading="To"
            trailing="Years"
            :min="0"
            :model="job.experienceRangeMax"
            @input="(experienceRangeMax) => (job.experienceRangeMax = experienceRangeMax)"
          />
        </div>
      </div>

      <NumberInput
        label="Salary Budget"
        icon="pi-money-bill"
        :model="job.salaryBudget"
        :min="0"
        trailing="INR"
        @input="(salaryBudget) => (job.salaryBudget = salaryBudget)"
      />

      <TextArea label="Job Description" :model="job.description" />

      <div class="flex w-full flex-col gap-6 sm:flex-row">
        <NumberInput
          label="Payment per CV Upload"
          icon="pi-wallet"
          trailing="INR"
          :model="job.bonusPayPerCv"
          :min="0"
        />
        <DateInput
          label="Payment Date per CV Upload"
          icon="pi-wallet"
          :model="job.cvRatePaymentDate"
        />
      </div>

      <div class="flex w-full flex-col gap-6 sm:flex-row">
        <TextInput label="Candidate Joining Bonus" icon="pi-money-bill" :model="job.closureBonus" />
        <DateInput
          label="Candidate Joining Bonus Payment Date"
          icon="pi-money-bill"
          :model="job.closureBonusPaymentDate"
        />
      </div>

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

    <div class="flex w-full justify-between">
      <Button outlined label="Back" size="small" :loading="creatingJob" @click="router.go(-1)" />
      <Button label="Create Job" @click="create(job, toast)" />
    </div>
  </div>
</template>
