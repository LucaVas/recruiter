<script setup lang="ts">
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import { onMounted, ref } from 'vue';
import Tag from 'primevue/tag';
import Column from 'primevue/column';
import { FilterMatchMode, FilterOperator } from 'primevue/api';
import Dropdown from 'primevue/dropdown';
import { useJobStore } from '@/stores/job';
import { type Job } from '../stores/job/types';

const jobStore = useJobStore();
const loading = ref(false);
const contractTypes = ref([{ name: 'Permanent' }, { name: 'Temporary' }]);
const jobs = ref<Job[]>();
const filters = ref();

const initFilters = () => {
  filters.value = {
    global: { value: null, matchMode: FilterMatchMode.CONTAINS },
    id: { value: null, matchMode: FilterMatchMode.EQUALS },
    client: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.STARTS_WITH }],
    },
    name: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.STARTS_WITH }],
    },
    skill: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.STARTS_WITH }],
    },
    contractType: { value: null, matchMode: FilterMatchMode.IN },
    experienceRange: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.STARTS_WITH }],
    },
    salaryBudget: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.GREATER_THAN_OR_EQUAL_TO }],
    },
    noticePeriodInDays: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.GREATER_THAN_OR_EQUAL_TO }],
    },
    numberOfCandidates: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.GREATER_THAN_OR_EQUAL_TO }],
    },
    creationDate: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.DATE_IS }],
    },
    status: { value: null, matchMode: FilterMatchMode.IN },
  };
};
initFilters();

const clearFilter = () => {
  initFilters();
};

const statuses = ref(['closed', 'open', 'pending']);
const getSeverity = (status: string) => {
  switch (status.toLowerCase()) {
    case 'closed':
      return 'danger';

    case 'open':
      return 'success';

    case 'pending':
      return 'warning';

    default:
      return 'info';
  }
};

const formatDate = (value: Date) => {
  return value.toLocaleDateString('en-US', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  });
};

onMounted(async () => {
  loading.value = true;
  jobs.value = await jobStore.getAllJobs();
  loading.value = false;
});
</script>

<template>
  <div class="card h-full w-full">
    <DataTable
      v-model:filters="filters"
      filterDisplay="menu"
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
      showGridlines
      paginator
      :rows="10"
      size="large"
      :loading="loading"
      dataKey="id"
      :rowsPerPageOptions="[5, 10, 20, 50]"
    >
      <template #header>
        <div class="justify-content-between flex w-full">
          <Button
            type="button"
            icon="pi pi-filter-slash"
            label="Clear"
            outlined
            @click="clearFilter()"
          />
          <IconField iconPosition="left">
            <InputIcon>
              <i class="pi pi-search" />
            </InputIcon>
            <InputText v-model="filters['global'].value" placeholder="Keyword Search" />
          </IconField>
        </div>
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
      <Column field="client" header="Client" class="min-w-20">
        <template #body="{ data }">
          {{ data.client }}
        </template>
        <template #filter="{ filterModel }">
          <InputText
            v-model="filterModel.value"
            type="text"
            class="p-column-filter"
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
      <Column field="skill" header="skill" class="min-w-10">
        <template #body="{ data }">
          {{ data.skill }}
        </template>
        <template #filter="{ filterModel }">
          <InputText
            v-model="filterModel.value"
            type="text"
            class="p-column-filter"
            placeholder="Search by job skill"
          />
        </template>
      </Column>
      <Column field="contractType" header="Contract" class="min-w-20">
        <template #body="{ data }">
          {{ data.contractType }}
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
          {{ data.experienceRange }}
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
      <Column field="salaryBudget" header="salaryBudget" class="min-w-10">
        <template #body="{ data }">
          {{ data.salaryBudget }}
        </template>
        <template #filter="{ filterModel }">
          <InputText
            v-model="filterModel.value"
            type="number"
            class="p-column-filter"
            placeholder="Search by salaryBudget"
          />
        </template>
      </Column>
      <Column field="noticePeriodInDays" header="Notice Period" dataType="numeric" class="min-w-20">
        <template #body="{ data }">
          {{ data.noticePeriodInDays }}
        </template>
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
      <Column field="creationDate" header="Creation Date" dataType="date" class="min-w-10">
        <template #body="{ data }">
          {{ formatDate(data.creationDate) }}
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
  </div>
</template>
