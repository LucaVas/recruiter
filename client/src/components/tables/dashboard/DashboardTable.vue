<script setup lang="ts">
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import ActionButtonsColumn from './columns/ActionButtonsColumn.vue';
import { onMounted, ref } from 'vue';
import Tag from 'primevue/tag';
import Column from 'primevue/column';
import Dropdown from 'primevue/dropdown';
import type { Job } from '@/stores/job/types';
import Toast from 'primevue/toast';
import { formatDate } from '@/utils/dateUtils';
import {
  filters,
  initFilters,
  getSeverity,
  getContractType,
  getClientIcon,
  globalFiltersFields,
  clearFilter,
  getSkills,
} from './utils';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '@/utils/types';
import { getAllJobs } from '@/stores/job';
import Header from '../shared/Header.vue';

const loading = ref(false);
const contractTypes = ref([{ name: 'Permanent' }, { name: 'Temporary' }]);
const jobs = ref<Job[]>();
const statuses = ref(['closed', 'open', 'pending']);
const showAllColumns = ref(false);

const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

async function initTable() {
  loading.value = true;
  try {
    jobs.value = await getAllJobs();
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
    :globalFilterFields="globalFiltersFields"
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
      <Header
        :filters="filters"
        :showColumns="showAllColumns"
        @clearFilter="clearFilter()"
        @showOrHideColumns="showAllColumns = !showAllColumns"
      />
    </template>
    <template #empty> No jobs found. </template>
    <template #loading> Loading jobs, please wait... </template>

    <Column field="action" header="" class="min-w-fit">
      <template #body="{ data }">
        <ActionButtonsColumn
          :data="data"
          @reload-table="initTable()"
          @pass-error="(message) => showError(message)"
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
    <Column field="skills" header="Skills" class="min-w-52" v-if="showAllColumns">
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
    <Column field="contractType" header="Contract" class="min-w-20" v-if="showAllColumns">
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
    <Column field="experienceRange" header="Experience" class="min-w-20" v-if="showAllColumns">
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
    <Column field="salaryBudget" header="Salary Budget" class="min-w-44" v-if="showAllColumns">
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
    <Column
      field="noticePeriodInDays"
      header="Notice Period"
      dataType="numeric"
      class="min-w-40"
      v-if="showAllColumns"
    >
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
    <Column
      field="creationDate"
      header="Creation Date"
      dataType="date"
      class="min-w-40"
      v-if="showAllColumns"
    >
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
  </DataTable>
</template>
@/stores/job/schema
