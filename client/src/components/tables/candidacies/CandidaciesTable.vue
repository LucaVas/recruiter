<template>
  <Toast />
  <DataTable
    v-model:filters="filters"
    filterDisplay="menu"
    size="small"
    :globalFilterFields="columns"
    :value="candidates"
    stripedRows
    paginator
    :rows="10"
    :loading="loadingTable"
    dataKey="id"
    :rowsPerPageOptions="[5, 10, 20, 50]"
    class="w-full"
    tableStyle="margin-top: 1rem; margin-bottom: 1rem; font-size: 0.875rem; line-height: 1.25rem;"
  >
    <template #header>
      <Header :filters="filters" @clearFilter="clearFilter()" />
    </template>
    <template #empty> No candidacies found. </template>
    <template #loading> Loading candidacies, please wait... </template>

    <Column field="id" header="ID" dataType="numeric" class="min-w-16">
      <template #body="{ data }">
        {{ data.id }}
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter"
          placeholder="Search by user id"
        />
      </template>
    </Column>
    <Column field="job" header="Job" class="min-w-52">
      <template #body="{ data }">
        <div class="flex items-center gap-3">
          {{ data.job.name }}
        </div>
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by job"
        />
      </template>
    </Column>
    <Column field="client" header="Client" class="min-w-52">
      <template #body="{ data }">
        <div class="flex items-center gap-3">
          {{ data.job.client }}
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
    <Column field="candidate" header="Candidate" class="min-w-52">
      <template #body="{ data }">
        <div class="flex items-center gap-3">
          {{ data.candidate.email }}
        </div>
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by candidate"
        />
      </template>
    </Column>
    <Column field="recruiter" header="Recruiter" class="min-w-52">
      <template #body="{ data }">
        <div class="flex items-center gap-3">
          {{ data.recruiter.username }}
        </div>
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by recruiter"
        />
      </template>
    </Column>
    <Column field="createdOn" header="Submitted On" dataType="date" class="min-w-40">
      <template #body="{ data }">
        {{ formatDate(data.createdAt) }}
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="date"
          class="p-column-filter"
          placeholder="Search by submission date"
        />
      </template>
    </Column>

    <Column field="action" header="" class="min-w-10">
      <template #body="{ data }">
        <div class="flex gap-2">
          <Button
            label="Edit"
            class="h-8 min-w-fit"
            rounded
            @click="router.push({ name: 'Candidacy', params: { id: data.id } })"
          />
        </div>
      </template>
    </Column>
  </DataTable>
</template>

<script setup lang="ts">
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Toast from 'primevue/toast';
import { onMounted, ref } from 'vue';
import Column from 'primevue/column';
import { filters, initFilters, clearFilter } from './filters';
import { ApiError } from '@/utils/types';
import { formatDate } from './utils';
import { getAllCandidacies } from '@/stores/candidacy';
import { useToast } from 'primevue/usetoast';
import { columns } from '.';
import Header from '../shared/Header.vue';
import type { CandidacyDto } from '@/stores/candidacy/types';
import { useRouter } from 'vue-router';

const router = useRouter();
const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

const loadingTable = ref(false);
const candidates = ref<CandidacyDto[]>();

async function initTable() {
  loadingTable.value = true;
  try {
    candidates.value = await getAllCandidacies();
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
  } finally {
    loadingTable.value = false;
  }
}
initFilters();
onMounted(async () => {
  await initTable();
});
</script>
