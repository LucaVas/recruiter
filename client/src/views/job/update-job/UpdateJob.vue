<script setup lang="ts">
import { delJob, createNewSkill, skillModalOpen, creatingSkill } from '../jobCommons';
import JobInformation from '@/components/job/shared/JobInformation.vue';
import JobHiringDetails from '@/components/job/shared/JobHiringDetails.vue';
import JobPaymentDetails from '@/components/job/shared/JobPaymentDetails.vue';
import JobStatusComponent from '@/components/job/update-job/JobStatusComponent.vue';
import { ref, onBeforeMount } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ProgressSpinner from 'primevue/progressspinner';
import type { JobStatus } from '@/stores/job/schema';
import { useToast } from 'primevue/usetoast';
import {
  updatingJob,
  changeStatus,
  initializeJob,
  update,
  job,
  clients,
  skills,
  addSkill,
  removeSkill,
} from './index';
import NewSkillModal from '@/components/skill/NewSkillModal.vue';
import SkillsDropdown from '@/components/job/shared/SkillsDropdown.vue';
import JobSkills from '@/components/job/job-page/JobSkills.vue';
import type { NewSkill, Skill } from '@/stores/skill/schema';

// variables
const route = useRoute();
const jobId = ref(route.params.id);
const router = useRouter();
const toast = useToast();

// init
onBeforeMount(async () => await initializeJob(Number(jobId.value), toast));
</script>

<template>
  <div v-if="job" class="flex w-full flex-col gap-8 pb-6">
    <JobStatusComponent
      :status="job.status"
      :createdAt="job.createdDTime"
      @delete="delJob(job.id, router, toast)"
      @changeStatus="(status: JobStatus) => changeStatus(job!.id, status, toast)"
    />
    <JobInformation
      :jobInformation="{
        client: job.client,
        name: job.name,
        status: job.status,
        contractType: job.contractType,
      }"
      :clients="clients"
      @input="
        (details) => {
          if (!job) return;
          job.client = details.client;
          job.name = details.name;
          job.status = details.status;
          job.contractType = details.contractType;
        }
      "
      @selectClient="
        (client) => {
          if (!job) return;
          job.client = client;
          clients.push(client);
        }
      "
    />
    <JobHiringDetails
      :jobHiringDetails="{
        wantedCvs: job.wantedCvs,
        noticePeriodInDays: job.noticePeriodInDays,
        experienceRangeMin: job.experienceRangeMin,
        experienceRangeMax: job.experienceRangeMax,
        salaryBudget: job.salaryBudget,
        currency: job.currency,
        description: job.description,
      }"
      @input="
        (details) => {
          if (!job) return;
          job.wantedCvs = details.wantedCvs;
          job.noticePeriodInDays = details.noticePeriodInDays;
          job.experienceRangeMin = details.experienceRangeMin;
          job.experienceRangeMax = details.experienceRangeMax;
          job.salaryBudget = details.salaryBudget;
          job.currency = details.currency;
          job!.description = details.description;
        }
      "
    />
    <JobPaymentDetails
      :jobPaymentDetails="{
        bonusPayPerCv: job.bonusPayPerCv,
        cvRatePaymentDate: job.cvRatePaymentDate,
        closureBonus: job.closureBonus,
        closureBonusPaymentDate: job.closureBonusPaymentDate,
      }"
      @input="
        (details) => {
          if (!job) return;
          job.bonusPayPerCv = details.bonusPayPerCv;
          job.cvRatePaymentDate = details.cvRatePaymentDate;
          job.closureBonus = details.closureBonus;
          job.closureBonusPaymentDate = details.closureBonusPaymentDate;
          console.log(job);
        }
      "
    />
    <div class="space-y-3">
      <NewSkillModal
        :visible="skillModalOpen"
        :creatingSkill="creatingSkill"
        @close="skillModalOpen = false"
        @save="(skill: NewSkill) => createNewSkill(skill, toast)"
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
    <div class="flex w-full justify-between">
      <Button outlined label="Back" size="small" :loading="updatingJob" @click="router.go(-1)" />
      <Button label="Update Job" @click="update(job, toast)" />
    </div>
  </div>

  <div v-else class="flex w-full items-center justify-center">
    <ProgressSpinner />
  </div>
</template>
