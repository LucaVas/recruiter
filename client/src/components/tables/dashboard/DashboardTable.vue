<script setup lang="ts">
import DataTable from 'primevue/datatable';
import ActionButtonsColumn from './columns/ActionButtonsColumn.vue';
import { onMounted, ref } from 'vue';
import Column from 'primevue/column';
import { filters, initFilters, globalFiltersFields } from './utils';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '@/utils/types';
import Header from '../shared/Header.vue';
import { showError } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import DashboardJobCard from '@/components/job/jobs-table/DashboardJobCard.vue';
import DashboardJobInfoCard from '@/components/job/jobs-table/DashboardJobInfoCard.vue';
import { formatJobStatus, getJobSeverity } from '@/components/job/utils';
import useJobStore from '@/stores/jobStore';
import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from '../../../consts';

const jobStore = useJobStore();

const toast = useToast();
const loading = ref(false);
async function initTable(pageNumber: number, pageSize: number) {
  loading.value = true;
  try {
    await jobStore.loadJobs(pageNumber, pageSize);
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    loading.value = false;
  }
}

initFilters();

onMounted(async () => {
  await initTable(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
});
</script>

<template>
  <DataTable
    v-model:filters="filters"
    filterDisplay="menu"
    size="small"
    :globalFilterFields="globalFiltersFields"
    :value="jobStore.jobs"
    stripedRows
    :loading="loading"
    dataKey="id"
    class="w-full"
    tableStyle="margin-top: 1rem; margin-bottom: 1rem; font-size: 0.875rem; line-height: 1.25rem;"
  >
    <template #header>
      <Header :filters="filters" @refresh="initTable(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)" />
    </template>
    <template #empty> No jobs found. </template>
    <template #loading> Loading jobs, please wait... </template>

    <Column class="min-w-fit text-center">
      <template #body="{ data }">
        <ActionButtonsColumn
          :data="data"
          @reloadTable="initTable(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)"
        />
      </template>
    </Column>

    <Column header="Job" class="min-w-52">
      <template #body="{ data }">
        <DashboardJobCard :job="data" />
      </template>
    </Column>

    <Column header="Information" class="min-w-80">
      <template #body="{ data }">
        <DashboardJobInfoCard :job="data" />
      </template>
    </Column>

    <Column header="Status" class="min-w-fit">
      <template #body="{ data }">
        <Tag :value="formatJobStatus(data.status)" :severity="getJobSeverity(data.status)" />
      </template>
    </Column>

    <Column class="hidden">
      <template #body="{ data }">
        {{ data.client.name }}
      </template>
    </Column>
    <Column class="hidden">
      <template #body="{ data }">
        {{ data.name }}
      </template>
    </Column>
    <Column class="hidden">
      <template #body="{ data }"> {{ data.salaryBudget }}</template>
    </Column>
    <Column dataType="numeric" class="hidden">
      <template #body="{ data }"> {{ data.noticePeriodInDays }} </template>
    </Column>
  </DataTable>
  <Paginator
    :rows="5"
    :totalRecords="jobStore.totalJobs"
    :rowsPerPageOptions="[5, 10, 20, 30]"
    @page="(event) => initTable(event.page, event.rows)"
  />
</template>
