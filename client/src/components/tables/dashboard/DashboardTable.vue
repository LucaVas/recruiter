<script setup lang="ts">
import DataTable from 'primevue/datatable';
import ActionButtonsColumn from './columns/ActionButtonsColumn.vue';
import { onMounted, ref } from 'vue';
import Column from 'primevue/column';
import type { Job } from '@/stores/job/schema';
import { filters, initFilters, globalFiltersFields } from './utils';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '@/utils/types';
import { getAllJobs } from '@/stores/job';
import Header from '../shared/Header.vue';
import { showError } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import DashboardJobCard from '@/components/job/jobs-table/DashboardJobCard.vue';
import DashboardJobInfoCard from '@/components/job/jobs-table/DashboardJobInfoCard.vue';
import { formatJobStatus, getJobSeverity } from '@/components/job/utils';

const loading = ref(false);
const jobs = ref<Job[]>();

const toast = useToast();

async function initTable() {
  loading.value = true;
  try {
    jobs.value = await getAllJobs();
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
  await initTable();
});
</script>

<template>
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
      <Header :filters="filters" @refresh="initTable()" />
    </template>
    <template #empty> No jobs found. </template>
    <template #loading> Loading jobs, please wait... </template>

    <Column class="min-w-fit text-center">
      <template #body="{ data }">
        <ActionButtonsColumn :data="data" @reloadTable="initTable()" />
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
</template>
