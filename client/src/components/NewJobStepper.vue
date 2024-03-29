<script setup lang="ts">
import Stepper from 'primevue/stepper';
import StepperPanel from 'primevue/stepperpanel';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import Button from 'primevue/button';
import SkillsList from '@/components/SkillsList.vue';

import { ref } from 'vue';
import type { NewJob, JobStatus, Currency } from '../stores/job/types';
import Dropdown from 'primevue/dropdown';
import type { RawSkill } from '../stores/skill/types';
import { useJobStore } from '@/stores/job';
import type { ContractTypeName } from '../stores/job/types';

const jobStore = useJobStore();
const skills = ref<RawSkill[]>([]);
const experienceRangeValues = ref([0, 45]);
const jobStatuses = ref<{ name: string; label: string; value: JobStatus }[]>([
  { name: 'Open', label: 'job_open.png', value: 'OPEN' },
  { name: 'Closed', label: 'job_closed.png', value: 'CLOSED' },
  { name: 'No CVs Accepted', label: 'job_no_cv_accepted.png', value: 'NO_CV_ACCEPTED' },
]);
const currencies = ref<{ name: string; value: Currency }[]>([{ name: 'INR', value: 'INR' }]);
const contracTypes = ref<{ name: string; value: ContractTypeName }[]>([
  { name: 'Permanent', value: 'PERMANENT' },
  { name: 'Temporary', value: 'TEMPORARY' },
]);
const newJobForm = ref({
  client: '',
  name: '',
  status: {} as (typeof jobStatuses.value)[0],
  contractType: {} as (typeof contracTypes.value)[0],
  wantedCvs: '',
  noticePeriodInDays: '',
  salaryBudget: '',
  currency: {} as (typeof currencies.value)[0],
  description: '',
  bonusPayPerCv: '',
  closureBonus: '',
  comments: '',
});

type NewJobForm = typeof newJobForm;
function formToNewJob(newJobForm: NewJobForm): NewJob {
  return {
    ...newJobForm.value,
    skills: skills.value,
    status: newJobForm.value.status.value,
    currency: newJobForm.value.currency.value,
    contractType: newJobForm.value.contractType.value ,
    experienceRangeMin: experienceRangeValues.value[0],
    experienceRangeMax: experienceRangeValues.value[1],
    wantedCvs: Number(newJobForm.value.wantedCvs),
    noticePeriodInDays: Number(newJobForm.value.noticePeriodInDays),
    salaryBudget: Number(newJobForm.value.salaryBudget),
    bonusPayPerCv: Number(newJobForm.value.bonusPayPerCv),
  };
}

async function submitNewJob() {
  const newJob = formToNewJob(newJobForm);
  console.log(newJob);
  await jobStore.addJob(newJob);
}

function removeSkill(skill: RawSkill): void {
  const idx = skills.value.indexOf(skill);
  if (idx > -1) {
    skills.value.splice(idx, 1);
  }
}

const active = ref(0);
</script>

<template>
  <Stepper v-model:activeStep="active" class="flex h-full w-[50rem] flex-col justify-start">
    <!-- Job main info -->
    <StepperPanel>
      <template #header="{ index, clickCallback }">
        <button class="flex flex-col border-none bg-transparent" @click="clickCallback">
          <span
            :class="[
              'border-round w-3rem h-3rem align-items-center justify-content-center inline-flex border-2',
              { 'bg-primary border-primary': index <= active, 'surface-border': index > active },
            ]"
          >
            <i class="pi pi-user" />
          </span>
        </button>
      </template>

      <template #content="{ nextCallback }">
        <div class="flex h-full w-full flex-col justify-between gap-6">
          <div class="flex flex-col gap-6">
            <div class="mb-3 mt-3 text-center text-xl font-semibold">Add Job Information</div>
            <div class="field p-fluid">
              <IconField>
                <InputIcon>
                  <i class="pi pi-user" />
                </InputIcon>
                <InputText
                  id="jobClient"
                  v-model="newJobForm.client"
                  type="text"
                  placeholder="Client Name"
                />
              </IconField>
            </div>
            <div class="field p-fluid">
              <IconField>
                <InputIcon>
                  <i class="pi pi-briefcase" />
                </InputIcon>
                <InputText
                  id="jobName"
                  v-model="newJobForm.name"
                  type="text"
                  placeholder="Job Name"
                />
              </IconField>
            </div>
            <div class="field p-fluid">
              <Dropdown
                v-model="newJobForm.status"
                :options="jobStatuses"
                optionLabel="name"
                dropdown-icon="pi pi-angle-down"
                placeholder="Select a Status"
                class="w-full"
              />
            </div>
            <div class="field p-fluid">
              <Dropdown
                v-model="newJobForm.contractType"
                :options="contracTypes"
                optionLabel="name"
                dropdown-icon="pi pi-angle-down"
                placeholder="Select a Contract Type"
                class="w-full"
              />
            </div>
          </div>
          <div class="flex justify-end pt-4">
            <Button label="Next" icon="pi pi-arrow-right" iconPos="right" @click="nextCallback" />
          </div>
        </div>
      </template>
    </StepperPanel>
    <!-- Job details -->
    <StepperPanel>
      <template #header="{ index, clickCallback }">
        <button
          class="flex-column inline-flex gap-2 border-none bg-transparent"
          @click="clickCallback"
        >
          <span
            :class="[
              'border-round w-3rem h-3rem align-items-center justify-content-center inline-flex border-2',
              { 'bg-primary border-primary': index <= active, 'surface-border': index > active },
            ]"
          >
            <i class="pi pi-star" />
          </span>
        </button>
      </template>
      <template #content="{ prevCallback, nextCallback }">
        <div class="flex h-full w-full flex-col justify-between gap-6">
          <div class="flex flex-col gap-4">
            <div class="mb-3 mt-3 text-center text-xl font-semibold">Add Job Details</div>
            <div class="field p-fluid w-full">
              <IconField>
                <InputIcon>
                  <i class="pi pi-file" />
                </InputIcon>
                <InputText
                  id="wantedCvs"
                  v-model="newJobForm.wantedCvs"
                  type="number"
                  placeholder="Number of CVs wanter"
                  required
                />
              </IconField>
            </div>
            <div class="field mb-3 flex w-full flex-col justify-center gap-5">
              <div class="flex flex-row items-center justify-between">
                <span>{{ experienceRangeValues[0] }} years</span>
                <span class="font-medium">Experience range</span>
                <span>{{ experienceRangeValues[1] }} years</span>
              </div>
              <div class="px-1">
                <Slider v-model="experienceRangeValues" :max="45" range class="w-14rem" />
              </div>
            </div>
            <div class="field p-fluid w-full">
              <IconField>
                <InputIcon>
                  <i class="pi pi-calendar-times" />
                </InputIcon>
                <InputText
                  id="noticePeriodInDays"
                  v-model="newJobForm.noticePeriodInDays"
                  type="number"
                  placeholder="Notice Period (days)"
                  required
                />
              </IconField>
            </div>
            <div class="field p-fluid flex w-full gap-2">
              <IconField class="w-full">
                <InputIcon>
                  <i class="pi pi-money-bill" />
                </InputIcon>
                <InputText
                  id="salaryBudget"
                  v-model="newJobForm.salaryBudget"
                  type="number"
                  placeholder="Salary Budget"
                  required
                />
              </IconField>
              <Dropdown
                v-model="newJobForm.currency"
                :options="currencies"
                optionLabel="name"
                dropdown-icon="pi pi-angle-down"
                class="w-40"
                required
              />
            </div>
            <div>
              <Textarea
                v-model="newJobForm.description"
                class="w-full"
                rows="4"
                cols="30"
                placeholder="Tell us what is the job about..."
                style="resize: none"
                required
              />
            </div>
          </div>
          <div class="flex justify-between pt-4">
            <Button
              label="Back"
              severity="secondary"
              icon="pi pi-arrow-left"
              @click="prevCallback"
            />
            <Button label="Next" icon="pi pi-arrow-right" iconPos="right" @click="nextCallback" />
          </div>
        </div>
      </template>
    </StepperPanel>
    <!-- Skills -->
    <StepperPanel>
      <template #header="{ index, clickCallback }">
        <button
          class="flex-column inline-flex gap-2 border-none bg-transparent"
          @click="clickCallback"
        >
          <span
            :class="[
              'border-round w-3rem h-3rem align-items-center justify-content-center inline-flex border-2',
              { 'bg-primary border-primary': index <= active, 'surface-border': index > active },
            ]"
          >
            <i class="pi pi-briefcase" />
          </span>
        </button>
      </template>
      <template #content="{ prevCallback, nextCallback }">
        <div class="flex h-full w-full flex-col justify-between gap-6">
          <div class="flex flex-col gap-4">
            <div class="my-3 text-center text-xl font-semibold">Skills required</div>
            <!-- Skill chips -->
            <div
              v-if="skills.length > 0"
              class="field my-4 flex max-h-24 min-h-9 w-full flex-wrap gap-3"
            >
              <Chip
                v-for="skill in skills"
                v-bind:key="skill.id"
                :label="skill.name"
                icon="pi pi-check"
                removable
                class="bg-[#3b80f64d]"
                @remove="removeSkill(skill)"
              />
            </div>
            <div v-else class="field my-4 flex h-9 w-full flex-wrap gap-3">No skills selected.</div>

            <!-- Skill list -->
            <div class="flex flex-col gap-4">
              <SkillsList @updateSelection="(selectedSkills) => (skills = selectedSkills)" />
            </div>
          </div>

          <div class="flex justify-between pt-4">
            <Button
              label="Back"
              severity="secondary"
              icon="pi pi-arrow-left"
              @click="prevCallback"
            />
            <Button label="Next" icon="pi pi-arrow-right" iconPos="right" @click="nextCallback" />
          </div>
        </div>
      </template>
    </StepperPanel>
    <!-- Final details -->
    <StepperPanel>
      <template #header="{ index, clickCallback }">
        <button
          class="flex-column inline-flex gap-2 border-none bg-transparent"
          @click="clickCallback"
        >
          <span
            :class="[
              'border-round w-3rem h-3rem align-items-center justify-content-center inline-flex border-2',
              { 'bg-primary border-primary': index <= active, 'surface-border': index > active },
            ]"
          >
            <i class="pi pi-id-card" />
          </span>
        </button>
      </template>
      <template #content="{ prevCallback }">
        <div class="mb-auto flex h-full w-full flex-col justify-between gap-6">
          <div class="flex flex-col gap-4">
            <div class="mb-3 mt-3 text-center text-xl font-semibold">Almost there...</div>
            <div class="field p-fluid w-full">
              <IconField>
                <InputIcon>
                  <i class="pi pi-dollar" />
                </InputIcon>
                <InputText
                  id="bonusPayPerCv"
                  v-model="newJobForm.bonusPayPerCv"
                  type="number"
                  placeholder="Bonus Pay per CV"
                  required
                />
              </IconField>
            </div>
            <div class="field p-fluid w-full">
              <IconField>
                <InputIcon>
                  <i class="pi pi-money-bill" />
                </InputIcon>
                <InputText
                  id="closureBonus"
                  v-model="newJobForm.closureBonus"
                  type="text"
                  placeholder="Closure Bonus"
                  required
                />
              </IconField>
            </div>
            <div>
              <Textarea
                v-model="newJobForm.comments"
                class="w-full"
                variant="filled"
                rows="5"
                cols="30"
                placeholder="Administrator Comments"
                required
                disabled
              />
            </div>
          </div>
          <div class="flex justify-between pt-4">
            <Button
              label="Back"
              severity="secondary"
              icon="pi pi-arrow-left"
              @click="prevCallback"
            />
            <Button
              label="Create new job"
              icon="pi pi-arrow-right"
              iconPos="right"
              @click="submitNewJob()"
            />
          </div>
        </div>
      </template>
    </StepperPanel>
  </Stepper>
</template>
