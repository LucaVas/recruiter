<script setup lang="ts">
import { delJob, createNewSkill, skillModalOpen, creatingSkill } from './jobCommons';
import JobStatusComponent from '@/components/job/UpdateJobStatus.vue';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ProgressSpinner from 'primevue/progressspinner';
import type { Job, JobStatus } from '@/stores/job/schema';
import { useToast } from 'primevue/usetoast';
import NewSkillModal from '@/components/skill/NewSkillModal.vue';
import SkillsDropdown from '@/components/job/SkillsDropdown.vue';
import JobSkills from '@/components/job/job-page/JobSkills.vue';
import type { Skill } from '@/stores/skill/schema';
import type { Client } from '@/stores/client/schema';
import { contractTypes } from '@/components/job/utils';
import JobClientSection from '@/components/job/JobClientSection.vue';
import NewQuestionnaireModal from '@/components/questionnaire/NewQuestionnaireModal.vue';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputGroup from 'primevue/inputgroup';
import InputText from 'primevue/inputtext';
import Textarea from 'primevue/textarea';
import PageHeaderBanner from '@/components/job/PageHeaderBanner.vue';
import type { ToastServiceMethods } from 'primevue/toastservice';
import { getAllClients } from '@/stores/client';
import { getAllSkills } from '@/stores/skill';
import { handleError, showSuccess } from '@/utils/errorUtils';
import { changeJobStatus, getJob, updateJob } from '@/stores/job';
import type { Questionnaire } from '@/stores/questionnaire/schema';
import { getAllQuestionnaires } from '@/stores/questionnaire/api';

const route = useRoute();
const jobId = ref(route.params.id);
const router = useRouter();
const toast = useToast();
const job = ref<Job>();
const clients = ref<Client[]>([]);
const skills = ref<Skill[]>([]);
const questionnaires = ref<Questionnaire[]>([]);

const initializingJob = ref(false);
const updatingJob = ref(false);
const jobUpdated = ref(false);
const changingStatus = ref(false);
const newQuestionnaireModalOpen = ref(false);
const isQuestionnaireUpdate = ref(false);

const loadQuestionnaires = async (toast: ToastServiceMethods) => {
  try {
    questionnaires.value = await getAllQuestionnaires();
  } catch (err) {
    handleError(toast, err);
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

const loadJobData = async (jobId: number, toast: ToastServiceMethods) => {
  try {
    job.value = await getJob(jobId);
  } catch (err) {
    handleError(toast, err);
  }
};

const initializeJob = async (jobId: number, toast: ToastServiceMethods) => {
  initializingJob.value = true;
  try {
    await Promise.all([
      loadJobData(jobId, toast),
      loadClients(toast),
      loadSkills(toast),
      loadQuestionnaires(toast),
    ]);
  } catch (err) {
    handleError(toast, err);
  } finally {
    initializingJob.value = false;
  }
};

const update = async (job: Job | undefined, toast: ToastServiceMethods) => {
  if (!job) return;
  updatingJob.value = true;
  try {
    await updateJob(job);
    jobUpdated.value = true;
  } catch (err) {
    handleError(toast, err);
  } finally {
    updatingJob.value = false;
  }
};

const changeStatus = async (id: number, status: JobStatus, toast: ToastServiceMethods) => {
  changingStatus.value = true;
  try {
    await changeJobStatus(id, status);
    showSuccess(toast, 'Job status changed successfully.');
    await initializeJob(id, toast);
  } catch (err) {
    handleError(toast, err);
  } finally {
    changingStatus.value = false;
  }
};

const addSkill = (job: Job | undefined, skill: Skill | undefined): void => {
  if (!job || !skill) return;
  if (job.skills.some((s: Skill) => s.name === skill.name)) return;
  job.skills.unshift(skill);
};

const removeSkill = (job: Job | undefined, skill: Skill): void => {
  if (!job) return;
  if (!job.skills.includes(skill)) return;
  job.skills.splice(job.skills.indexOf(skill), 1);
};

onMounted(async () => await initializeJob(Number(jobId.value), toast));
</script>

<template>
  <div v-if="!job" class="flex w-full items-center justify-center">
    <ProgressSpinner />
  </div>

  <div v-else class="flex w-full flex-col justify-evenly gap-3">
    <PageHeaderBanner title="Update Job" />

    <body class="flex flex-col gap-6">
      <JobStatusComponent
        :status="job.status"
        :createdAt="new Date(job.createdAt)"
        @delete="delJob(job.id, router, toast)"
        @changeStatus="(status: JobStatus) => changeStatus(job!.id, status, toast)"
      />

      <JobClientSection
        :client="job.client"
        :clients="clients"
        @select="(client: Client) => (job ? (job.client = client) : null)"
      />

      <!-- Name & Job status -->
      <div class="flex flex-col gap-6 sm:flex-row">
        <div class="flex w-full flex-col gap-2">
          <label class="text-sm">Job Name</label>
          <InputGroup>
            <InputGroupAddon><i class="pi pi-briefcase" /></InputGroupAddon>
            <InputText
              v-model="job.name"
              @update:modelValue="(name) => (name ? (job ? (job.name = name) : null) : '')"
            />
          </InputGroup>
        </div>

        <div class="flex w-full flex-col gap-2">
          <label class="text-sm">Contract Type</label>
          <InputGroup>
            <Dropdown
              v-model="job.contractType"
              :options="contractTypes"
              optionLabel="name"
              optionValue="value"
              class="w-full"
              @update:modelValue="
                (contractType) => (job ? (job.contractType = contractType) : null)
              "
            />
          </InputGroup>
        </div>
      </div>

      <!-- Wanted CVs & Notice Period -->
      <div class="flex flex-col gap-6 sm:flex-row">
        <div class="flex w-full flex-col gap-2">
          <label class="text-sm">Wanted CVs</label>
          <InputGroup>
            <InputGroupAddon><i class="pi pi-file" /></InputGroupAddon>
            <InputNumber
              v-model="job.wantedCvs"
              :min="0"
              @update:modelValue="(wantedCvs: number) => (job ? (job.wantedCvs = wantedCvs) : null)"
            />
          </InputGroup>
        </div>

        <div class="flex w-full flex-col gap-2">
          <label class="text-sm">Notice Period</label>
          <InputGroup>
            <InputGroupAddon><i class="pi pi-calendar-times" /></InputGroupAddon>
            <InputNumber
              v-model="job.noticePeriodInDays"
              :min="0"
              @update:modelValue="
                (noticePeriodInDays: number) =>
                  job ? (job.noticePeriodInDays = noticePeriodInDays) : null
              "
            />
            <InputGroupAddon class="min-w-fit">Days</InputGroupAddon>
          </InputGroup>
        </div>
      </div>

      <!-- experience range -->
      <div class="gap-2-row flex flex-col">
        <label class="text-sm">Experience range</label>
        <div class="flex flex-col gap-6 sm:flex-row">
          <div class="flex w-full flex-col gap-2">
            <InputGroup>
              <InputGroupAddon>From</InputGroupAddon>
              <InputNumber
                v-model="job.experienceRangeMin"
                :min="0"
                @update:modelValue="
                  (experienceRangeMin: number) =>
                    job ? (job.experienceRangeMin = experienceRangeMin) : null
                "
              />
              <InputGroupAddon class="min-w-fit">Years</InputGroupAddon>
            </InputGroup>
          </div>

          <div class="flex w-full flex-col gap-2">
            <InputGroup>
              <InputGroupAddon>To</InputGroupAddon>
              <InputNumber
                v-model="job.experienceRangeMax"
                :min="0"
                @update:modelValue="
                  (experienceRangeMax: number) =>
                    job ? (job.experienceRangeMax = experienceRangeMax) : null
                "
              />
              <InputGroupAddon class="min-w-fit">Years</InputGroupAddon>
            </InputGroup>
          </div>
        </div>
      </div>

      <!-- salary budget -->
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm">Salary Budget</label>
        <InputGroup>
          <InputGroupAddon><i class="pi pi-money-bill" /></InputGroupAddon>
          <InputNumber
            v-model="job.salaryBudget"
            :min="0"
            @update:modelValue="
              (salaryBudget: number) => (job ? (job.salaryBudget = salaryBudget) : null)
            "
          />
          <InputGroupAddon class="min-w-fit">INR</InputGroupAddon>
        </InputGroup>
      </div>

      <!-- description -->
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm">Job Description</label>
        <Textarea
          v-model="job.description"
          class="w-full"
          rows="4"
          cols="30"
          style="resize: none"
          @update:modelValue="(description) => (job ? (job.description = description) : null)"
        />
      </div>

      <!-- Payment per CV upload & date -->
      <div class="flex w-full flex-col gap-6 sm:flex-row">
        <div class="flex w-full flex-col gap-2">
          <label class="text-sm">Payment per CV Upload</label>
          <InputGroup>
            <InputGroupAddon><i class="pi pi-wallet" /></InputGroupAddon>
            <InputNumber
              v-model="job.bonusPayPerCv"
              :min="0"
              @update:modelValue="
                (bonusPayPerCv: number) => (job ? (job.bonusPayPerCv = bonusPayPerCv) : null)
              "
            />
            <InputGroupAddon class="min-w-fit">INR</InputGroupAddon>
          </InputGroup>
        </div>

        <div class="flex w-full flex-col gap-2">
          <label class="text-sm">Payment Date per CV Upload</label>
          <InputGroup>
            <InputGroupAddon><i class="pi pi-wallet" /></InputGroupAddon>
            <Calendar
              v-model="job.cvRatePaymentDate"
              showIcon
              :minDate="new Date()"
              dateFormat="dd/mm/yy"
              iconDisplay="input"
              inputId="icondisplay"
              required
              @select-date="
                (cvRatePaymentDate: Date) =>
                  job ? (job.cvRatePaymentDate = cvRatePaymentDate) : null
              "
              touchUI
            />
          </InputGroup>
        </div>
      </div>

      <div class="flex w-full flex-col gap-6 sm:flex-row">
        <div class="flex w-full flex-col gap-2">
          <label class="text-sm">Candidate Joining Bonus</label>
          <InputGroup>
            <InputGroupAddon><i class="pi pi-money-bill" /></InputGroupAddon>
            <InputText
              v-model="job.closureBonus"
              @update:modelValue="
                (closureBonus) =>
                  closureBonus ? (job ? (job.closureBonus = closureBonus) : null) : ''
              "
            />
          </InputGroup>
        </div>

        <div class="flex w-full flex-col gap-2">
          <label class="text-sm">Candidate Joining Bonus Payment Date</label>
          <InputGroup>
            <InputGroupAddon><i class="pi pi-money-bill" /></InputGroupAddon>
            <Calendar
              v-model="job.closureBonusPaymentDate"
              showIcon
              :minDate="new Date()"
              dateFormat="dd/mm/yy"
              iconDisplay="input"
              inputId="icondisplay"
              required
              @select-date="
                (closureBonusPaymentDate: Date) =>
                  job ? (job.closureBonusPaymentDate = closureBonusPaymentDate) : null
              "
              touchUI
            />
          </InputGroup>
        </div>
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
          :visible="newQuestionnaireModalOpen"
          :isUpdate="isQuestionnaireUpdate"
          :client="job.client"
          :questionnaire="job.questionnaire"
          @close="newQuestionnaireModalOpen = false"
          @select="
            async (q: Questionnaire) => {
              job ? (job.questionnaire = q) : null;
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
            @selectQuestionnaire="(q: Questionnaire) => (job ? (job.questionnaire = q) : null)"
          />
          <Button
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
          v-if="job.questionnaire"
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
    <footer class="flex justify-between">
      <Button outlined label="Back" size="small" :loading="updatingJob" @click="router.go(-1)" />
      <Button label="Update Job" @click="update(job, toast)" />
    </footer>
  </div>

  <Success
    :visible="jobUpdated"
    :title="'Job updated!'"
    :message="'Job is updated, and you will see it shortly in your dashboard'"
    @close="
      {
        jobUpdated = false;
        router.push({ name: 'Dashboard' });
      }
    "
  />
</template>
