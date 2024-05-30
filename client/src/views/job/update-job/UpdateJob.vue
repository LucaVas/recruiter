<script setup lang="ts">
import { delJob, createNewSkill, skillModalOpen, creatingSkill } from '../jobCommons';
import JobStatusComponent from '@/components/job/update-job/JobStatusComponent.vue';
import { ref, onMounted } from 'vue';
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
import type { Client } from '@/stores/client/schema';
import { contractTypes } from '@/components/job/shared/utils';
import JobClientSection from '@/components/job/JobClientSection.vue';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputGroup from 'primevue/inputgroup';
import InputText from 'primevue/inputtext';
import JobQuestionnaire from '@/components/questionnaire/JobQuestionnaire.vue';
import Textarea from 'primevue/textarea';

const route = useRoute();
const jobId = ref(route.params.id);
const router = useRouter();
const toast = useToast();

onMounted(async () => await initializeJob(Number(jobId.value), toast));
</script>

<template>
  <div v-if="job" class="flex w-full flex-col gap-8 pb-6">
    {{ job }}
    <JobStatusComponent
      :status="job.status"
      :createdAt="new Date(job.createdDTime)"
      @delete="delJob(job.id, router, toast)"
      @changeStatus="(status: JobStatus) => changeStatus(job!.id, status, toast)"
    />

    <JobClientSection
      :client="job.client"
      :clients="clients"
      @select="(client: Client) => (job.client = client)"
    />

    <!-- Name & Job status -->
    <div class="flex flex-col gap-6 sm:flex-row">
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm">Job Name</label>
        <InputGroup>
          <InputGroupAddon><i class="pi pi-briefcase" /></InputGroupAddon>
          <InputText
            v-model="job.name"
            @update:modelValue="(name) => (name ? (job.name = name) : '')"
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
            @change="(contractType) => (job.contractType = contractType)"
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
            @update:modelValue="(wantedCvs: number) => (job.wantedCvs = wantedCvs)"
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
              (noticePeriodInDays: number) => (job.noticePeriodInDays = noticePeriodInDays)
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
                (experienceRangeMin: number) => (job.experienceRangeMin = experienceRangeMin)
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
                (experienceRangeMax: number) => (job.experienceRangeMax = experienceRangeMax)
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
          @update:modelValue="(salaryBudget: number) => (job.salaryBudget = salaryBudget)"
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
        @update:modelValue="(description) => (job.description = description)"
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
            @update:modelValue="(bonusPayPerCv: number) => (job.bonusPayPerCv = bonusPayPerCv)"
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
            @update:modelValue="(cvRatePaymentDate) => (job.cvRatePaymentDate = cvRatePaymentDate)"
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
            @update:modelValue="(closureBonus) => (job.closureBonus = closureBonus)"
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
            @update:modelValue="
              (closureBonusPaymentDate) => (job.closureBonusPaymentDate = closureBonusPaymentDate)
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

    <JobQuestionnaire :questionnaire="job.questionnaire" @updateQuestionnaire="(q) => (job.questionnaire = q)" />

    <div class="flex w-full justify-between">
      <Button outlined label="Back" size="small" :loading="updatingJob" @click="router.go(-1)" />
      <Button label="Update Job" @click="update(job, toast)" />
    </div>
  </div>

  <div v-else class="flex w-full items-center justify-center">
    <ProgressSpinner />
  </div>
</template>
