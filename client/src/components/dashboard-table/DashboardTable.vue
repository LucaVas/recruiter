<script setup lang="ts">
import SplitButton from 'primevue/splitbutton';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import DeleteJobModal from './DeleteJobModal.vue';
import { onMounted, ref } from 'vue';
import Tag from 'primevue/tag';
import Column from 'primevue/column';
import Dropdown from 'primevue/dropdown';
import type { JobDto } from '@/stores/job/types';
import DashboardTableHeader from './DashboardTableHeader.vue';
import Toast from 'primevue/toast';
import {
  filters,
  initFilters,
  getSeverity,
  getContractType,
  getClientIcon,
  formatDate,
} from './utils';
import { getSkills, applyToJob } from './functions';
import { useJobStore } from '../../stores/job/index';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '../../utils/types';
import { useRouter } from 'vue-router';
import type { MenuItem } from 'primevue/menuitem';

const router = useRouter();
const toast = useToast();
const jobStore = useJobStore();
const loading = ref(false);
const contractTypes = ref([{ name: 'Permanent' }, { name: 'Temporary' }]);
const jobs = ref<JobDto[]>();
const statuses = ref(['closed', 'open', 'pending']);
const deleteJobModalOpen = ref(false);
const getSplitButtonChoices = function (): MenuItem[] {
  return [
    {
      label: 'Edit',
      icon: 'pi pi-file-edit',
      command: () => router.push({ name: 'Dashboard' }),
    },
    {
      label: 'Delete',
      icon: 'pi pi-times',
      command: async () => {
        deleteJobModalOpen.value = true;
      },
    },
  ];
};

const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

async function deleteJob(id: number) {
  try {
    await jobStore.deleteJob(id);
    initTable();
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
  } finally {
    loading.value = false;
    deleteJobModalOpen.value = false
  }
}

async function initTable() {
  loading.value = true;
  try {
    jobs.value = await jobStore.getAllJobs();
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
  } finally {
    loading.value = false;
  }
}

initFilters();
onMounted(async () => {
  await initTable();
});
</script>

<template>
  <Toast />

  <DataTable
    v-model:filters="filters"
    filterDisplay="menu"
    size="small"
    :globalFilterFields="[
      'id',
      'client',
      'name',
      'experienceRange',
      'contractType',
      'salaryBudget',
      'noticePeriodInDays',
      'numberOfCandidates',
      'creationDate',
      'status',
    ]"
    :value="jobs"
    stripedRows
    paginator
    :rows="5"
    :loading="loading"
    dataKey="id"
    :rowsPerPageOptions="[5, 10, 20, 50]"
    class="w-full"
    tableStyle="margin-top: 1rem; margin-bottom: 1rem; font-size: 0.875rem; line-height: 1.25rem;"
  >
    <template #header>
      <DashboardTableHeader />
    </template>
    <template #empty> No jobs found. </template>
    <template #loading> Loading jobs, please wait... </template>

    <Column field="id" header="ID" dataType="numeric" class="min-w-16">
      <template #body="{ data }">
        {{ data.id }}
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter"
          placeholder="Search by job id"
        />
      </template>
    </Column>
    <Column field="client" header="Client" class="min-w-52">
      <template #body="{ data }">
        <div class="flex items-center gap-3">
          <img :src="`src/assets/images/clients/${getClientIcon(data.client)}`" class="w-5" />
          {{ data.client }}
        </div>
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by client"
        />
      </template>
    </Column>
    <Column field="name" header="Job Name" class="min-w-52">
      <template #body="{ data }">
        {{ data.name }}
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter"
          placeholder="Search by job name"
        />
      </template>
    </Column>
    <Column field="skills" header="Skills" class="min-w-52">
      <template #body="{ data }">
        {{ getSkills(data.skills) }}
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by job skill"
        />
      </template>
    </Column>
    <Column field="contractType" header="Contract" class="min-w-20">
      <template #body="{ data }">
        <Tag
          :value="data.contractType.contractTypeName"
          :severity="getContractType(data.contractType.contractTypeName)"
        />
      </template>
      <template #filter="{ filterModel }">
        <MultiSelect
          v-model="filterModel.value"
          :options="contractTypes"
          optionLabel="name"
          placeholder="Any"
          class="p-column-filter"
        >
          <template #option="slotProps">
            <div class="align-items-center flex gap-2">
              <span>{{ slotProps.option.name }}</span>
            </div>
          </template>
        </MultiSelect>
      </template>
    </Column>
    <Column field="experienceRange" header="Experience" class="min-w-20">
      <template #body="{ data }">
        {{ data.experienceRangeMin }}-{{ data.experienceRangeMax }} years
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter"
          placeholder="Search by experience"
        />
      </template>
    </Column>
    <Column field="salaryBudget" header="Salary Budget" class="min-w-44">
      <template #body="{ data }"> {{ data.salaryBudget }} {{ data.currency }} </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="number"
          class="p-column-filter"
          placeholder="Search by salaryBudget"
        />
      </template>
    </Column>
    <Column field="noticePeriodInDays" header="Notice Period" dataType="numeric" class="min-w-40">
      <template #body="{ data }"> {{ data.noticePeriodInDays }} days </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="number"
          class="p-column-filter"
          placeholder="Search by notice period"
        />
      </template>
    </Column>
    <Column field="numberOfCandidates" header="Candidates" dataType="numeric" class="min-w-10">
      <template #body="{ data }">
        {{ data.numberOfCandidates }}
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="number"
          class="p-column-filter"
          placeholder="Search by candidates"
        />
      </template>
    </Column>
    <Column field="creationDate" header="Creation Date" dataType="date" class="min-w-40">
      <template #body="{ data }">
        {{ formatDate(data.createdAt) }}
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="date"
          class="p-column-filter"
          placeholder="Search by creation date"
        />
      </template>
    </Column>
    <Column field="status" header="Status" class="min-w-10">
      <template #body="{ data }">
        <Tag :value="data.status" :severity="getSeverity(data.status)" />
      </template>
      <template #filter="{ filterModel }">
        <Dropdown
          v-model="filterModel.value"
          :options="statuses"
          placeholder="Select One"
          class="p-column-filter"
          showClear
        >
          <template #option="slotProps">
            <Tag :value="slotProps.option" :severity="getSeverity(slotProps.option)" />
          </template>
        </Dropdown>
      </template>
    </Column>
    <Column field="action" header="" class="min-w-10">
      <template #body="{ data }">
        <DeleteJobModal
          :visible="deleteJobModalOpen"
          @closeModal="deleteJobModalOpen = false"
          @deleteJob="deleteJob(data.id)"
        />

        <SplitButton
          label="Apply"
          size="small"
          class="p-2"
          outlined
          severity="contrast"
          menuButtonIcon="pi pi-angle-down"
          @click="applyToJob(data.id)"
          :model="getSplitButtonChoices()"
        />
      </template>
    </Column>
  </DataTable>
</template>
