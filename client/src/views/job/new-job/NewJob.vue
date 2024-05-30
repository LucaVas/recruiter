<script setup lang="ts">
import JobClientSection from '@/components/job/JobClientSection.vue';
import SkillsDropdown from '@/components/job/SkillsDropdown.vue';
import JobSkills from '@/components/job/job-page/JobSkills.vue';
import JobQuestionnaire from '@/components/questionnaire/JobQuestionnaire.vue';
import { useToast } from 'primevue/usetoast';
import Divider from 'primevue/divider';
import { onMounted } from 'vue';
import Success from '@/components/Success.vue';
import PageHeaderBanner from '@/components/job/PageHeaderBanner.vue';
import type { NewSkill, Skill } from '@/stores/skill/schema';
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
} from './index';
import type NewSkillModal from '@/components/skill/NewSkillModal.vue';
import { createNewSkill, creatingSkill, skillModalOpen } from '../jobCommons';
import { useRouter } from 'vue-router';
import TextInput from '@/components/shared/TextInput.vue';
import NumberInput from '@/components/shared/NumberInput.vue';
import DateInput from '@/components/shared/DateInput.vue';
import TextArea from '@/components/shared/TextArea.vue';
import DropDown from '@/components/shared/DropDown.vue';
import { contractTypes, jobStatuses } from '@/components/job/utils';

const toast = useToast();
const router = useRouter();

onMounted(async () => {
  await Promise.all([loadClients(toast), loadSkills(toast)]);
});
</script>

<template>
  <div class="flex w-full flex-col">
    <PageHeaderBanner title="New Job" />

    <div class="flex flex-col gap-6">
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

      <JobQuestionnaire
        :questionnaire="job.questionnaire"
        @updateQuestionnaire="(q) => (job.questionnaire = q)"
      />
    </div>

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
