<script setup lang="ts">
// components
import TextInput from '@/components/shared/TextInput.vue';
import NumberInput from '@/components/shared/NumberInput.vue';
import DateInput from '@/components/shared/DateInput.vue';
import TextArea from '@/components/shared/TextArea.vue';
import DropDown from '@/components/shared/DropDown.vue';
import JobClientSection from '@/components/job/JobClientSection.vue';
import SkillsDropdown from '@/components/job/SkillsDropdown.vue';
import JobSkills from '@/components/job/job-page/JobSkills.vue';
import Success from '@/components/Success.vue';
import PageHeaderBanner from '@/components/job/PageHeaderBanner.vue';
import NewSkillModal from '@/components/skill/NewSkillModal.vue';
import QuestionnaireDropdown from '@/components/job/QuestionnaireDropdown.vue';
import NewQuestionnaireModal from '@/components/questionnaire/NewQuestionnaireModal.vue';
// primevue
import { useToast } from 'primevue/usetoast';
import Divider from 'primevue/divider';
import type { ToastServiceMethods } from 'primevue/toastservice';
// vue
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ref } from 'vue';
// stores
import type { Skill } from '@/stores/skill/schema';
import { getAllClients } from '@/stores/client';
import type { Client } from '@/stores/client/schema';
import { createJob } from '@/stores/job';
import type { NewJob } from '@/stores/job/schema';
import { getAllSkills } from '@/stores/skill';
import type { Questionnaire } from '@/stores/questionnaire/schema';
import { getAllQuestionnaires } from '@/stores/questionnaire/api';
// utils
import { handleError } from '@/utils/errorUtils';
import { createNewSkill, creatingSkill, skillModalOpen } from './jobCommons';
import { contractTypes, jobStatuses } from '@/components/job/utils';

const toast = useToast();
const router = useRouter();
const job = ref<NewJob>({
  name: '',
  client: {} as Client,
  status: 'OPEN',
  contractType: 'PERMANENT',
  wantedCvs: 0,
  noticePeriodInDays: 0,
  experienceRangeMin: 0,
  experienceRangeMax: 0,
  salaryBudget: 0,
  description: '',
  bonusPayPerCv: 0,
  currency: 'INR',
  cvRatePaymentDate: new Date(),
  closureBonus: 'Not Applicable',
  closureBonusPaymentDate: new Date(),
  skills: [],
  questionnaire: null,
});
const jobCreated = ref(false);
const creatingJob = ref(false);
const newQuestionnaireModalOpen = ref(false);
const isQuestionnaireUpdate = ref(false);

const clients = ref<Client[]>([]);
const skills = ref<Skill[]>([]);
const questionnaires = ref<Questionnaire[]>([]);

const removeSkill = (job: NewJob, skill: Skill): void => {
  if (!job.skills.includes(skill)) return;
  job.skills.splice(job.skills.indexOf(skill), 1);
};

const addSkill = (job: NewJob, skill: Skill | undefined): void => {
  if (!job || !skill) return;
  if (job.skills.some((s) => s.name === skill.name)) return;
  job.skills.unshift(skill);
};

const create = async (job: NewJob, toast: ToastServiceMethods): Promise<void> => {
  creatingJob.value = true;
  try {
    await createJob(job);
    jobCreated.value = true;
  } catch (err) {
    handleError(toast, err);
  } finally {
    creatingJob.value = false;
  }
};

const loadClients = async (toast: ToastServiceMethods) => {
  try {
    clients.value = await getAllClients();
  } catch (err) {
    handleError(toast, err);
  }
};

const loadSkills = async (toast: ToastServiceMethods) => {
  try {
    skills.value = await getAllSkills();
  } catch (err) {
    handleError(toast, err);
  }
};

const loadQuestionnaires = async (toast: ToastServiceMethods) => {
  try {
    questionnaires.value = await getAllQuestionnaires();
  } catch (err) {
    handleError(toast, err);
  }
};

onMounted(async () => {
  await Promise.all([loadClients(toast), loadSkills(toast), loadQuestionnaires(toast)]);
});
</script>

<template>
  <div class="flex w-full flex-col justify-evenly gap-3">
    <PageHeaderBanner title="New Job" />

    <body class="flex flex-col gap-6">
      <JobClientSection
        :client="job.client"
        :clients="clients"
        @select="
          async (client) => {
            job.client = client;
            await loadClients(toast);
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

      <TextArea
        label="Job Description"
        :model="job.description"
        @input="(description) => (job.description = description)"
      />

      <div class="flex w-full flex-col gap-6 sm:flex-row">
        <NumberInput
          label="Payment per CV Upload"
          icon="pi-wallet"
          trailing="INR"
          :model="job.bonusPayPerCv"
          @input="(bonusPayPerCv) => (job.bonusPayPerCv = bonusPayPerCv)"
          :min="0"
        />
        <DateInput
          label="Payment Date per CV Upload"
          icon="pi-wallet"
          :model="job.cvRatePaymentDate"
          @select="(cvRatePaymentDate) => (job.cvRatePaymentDate = cvRatePaymentDate)"
        />
      </div>

      <div class="flex w-full flex-col gap-6 sm:flex-row">
        <TextInput
          label="Candidate Joining Bonus"
          icon="pi-money-bill"
          :model="job.closureBonus"
          @input="(closureBonus) => (job.closureBonus = closureBonus)"
        />
        <DateInput
          label="Candidate Joining Bonus Payment Date"
          icon="pi-money-bill"
          :model="job.closureBonusPaymentDate"
          @select="
            (closureBonusPaymentDate) => (job.closureBonusPaymentDate = closureBonusPaymentDate)
          "
        />
      </div>

      <div class="space-y-3">
        <NewSkillModal
          :visible="skillModalOpen"
          :creatingSkill="creatingSkill"
          @close="skillModalOpen = false"
          @save="
            async (newSkill) => {
              const skill = await createNewSkill(newSkill, toast);
              addSkill(job, skill);
              await loadSkills(toast);
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
            class="hidden min-w-fit md:flex"
          />
          <Button icon="pi pi-plus" @click="skillModalOpen = true" class="min-w-fit md:hidden" />
        </div>

        <JobSkills
          :isNewJob="true"
          @remove="(skill: Skill) => removeSkill(job, skill)"
          :skills="job.skills"
        />
      </div>

      <div v-if="job.client.name" class="space-y-3">
        <NewQuestionnaireModal
          :questionnaire="job.questionnaire"
          :isUpdate="isQuestionnaireUpdate"
          :visible="newQuestionnaireModalOpen"
          :client="job.client"
          @close="newQuestionnaireModalOpen = false"
          @select="
            async (q: Questionnaire) => {
              job.questionnaire = q;
              await loadQuestionnaires(toast);
              newQuestionnaireModalOpen = false;
            }
          "
        />
        <label>Questionnaire</label>

        <div class="flex gap-3">
          <QuestionnaireDropdown
            :questionnaires="questionnaires"
            class="w-full"
            @selectQuestionnaire="(q: Questionnaire) => (job.questionnaire = q)"
          />
          <Button
            v-if="job.questionnaire == null"
            :disabled="!job.client.name"
            label="New"
            icon="pi pi-plus"
            @click="
              {
                isQuestionnaireUpdate = false;
                newQuestionnaireModalOpen = true;
              }
            "
            class="hidden min-w-fit md:flex"
          />
          <Button
            v-if="job.questionnaire == null"
            :disabled="!job.client.name"
            icon="pi pi-plus"
            @click="
              {
                isQuestionnaireUpdate = false;
                newQuestionnaireModalOpen = true;
              }
            "
            class="min-w-fit md:hidden"
          />
        </div>

        <div
          v-if="job.questionnaire && job.questionnaire.title !== ''"
          class="border-slate-150 mt-10 flex w-full items-center rounded-md border p-4"
        >
          <span class="flex min-w-fit gap-4">
            <Button unstyled icon="pi pi-trash" @click="job.questionnaire = null" />
            <Button
              unstyled
              icon="pi pi-file-edit"
              @click="
                {
                  isQuestionnaireUpdate = true;
                  newQuestionnaireModalOpen = true;
                }
              "
            />
          </span>
          <Divider layout="vertical" />
          <div class="space-x-2">
            <span>{{ job.questionnaire.client.name }}</span>
            <span>{{ job.questionnaire.title }}</span>
          </div>
        </div>
      </div>
    </body>

    <Divider />
    <div class="flex w-full justify-between">
      <Button outlined label="Back" size="small" :loading="creatingJob" @click="router.go(-1)" />
      <Button label="Create Job" @click="create(job, toast)" />
    </div>
  </div>

  <Success
    :visible="jobCreated"
    :title="'Job created!'"
    :message="'Job is created, and you will see it shortly in your dashboard'"
    @close="router.push({ name: 'Dashboard' })"
  />
</template>
