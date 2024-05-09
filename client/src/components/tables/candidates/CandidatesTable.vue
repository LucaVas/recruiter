<script setup lang="ts">
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Header from '../shared/Header.vue';
import { onMounted, ref } from 'vue';
import Column from 'primevue/column';
import { filters, initFilters, clearFilter } from './filters';
import { ApiError } from '@/utils/types';
import { formatDate } from '@/utils/dateUtils';
import { getAllCandidates, updateCandidate } from '@/stores/candidate';
import { useToast } from 'primevue/usetoast';
import type { Candidate, NewCandidateRequest } from '@/stores/candidate/schema';
import { columns } from '.';
import CandidateModal from '@/components/candidacy/candidate/shared/CandidateModal.vue';

const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

const loadingTable = ref(false);
const candidates = ref<Candidate[]>();
const editCandidateModal = ref(false);
const candidateToEdit = ref<NewCandidateRequest>();
const updatingCandidate = ref(false);
const showAllColumns = ref(false);

async function update(candidateForm: NewCandidateRequest) {
  updatingCandidate.value = true;
  try {
    await updateCandidate(candidateForm);
    editCandidateModal.value = false;
    await initTable();
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
  } finally {
    initFilters();
    updatingCandidate.value = false;
  }
}

async function initTable() {
  loadingTable.value = true;
  try {
    candidates.value = await getAllCandidates();
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

<template>
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
      <Header
        :showColumns="showAllColumns"
        :filters="filters"
        @clearFilter="clearFilter()"
        @showOrHideColumns="showAllColumns = !showAllColumns"
      />
    </template>
    <template #empty> No candidates found. </template>
    <template #loading> Loading candidates, please wait... </template>

    <Column field="action" header="" class="min-w-10">
      <template #body="{ data }">
        <div class="flex gap-2">
          <Button
            label="Edit"
            class="h-8 min-w-fit"
            rounded
            @click="
              candidateToEdit = data;
              editCandidateModal = true;
            "
          />
        </div>
      </template>
    </Column>
    <Column field="name" header="Name" class="min-w-52">
      <template #body="{ data }">
        <div class="flex items-center gap-3">
          {{ data.name }}
        </div>
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by name"
        />
      </template>
    </Column>
    <Column field="phone" header="Phone" class="min-w-52">
      <template #body="{ data }"> {{ data.phone }} </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by phone"
        />
      </template>
    </Column>
    <Column field="email" header="Email" class="min-w-52">
      <template #body="{ data }">
        {{ data.email }}
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter"
          placeholder="Search by email"
        />
      </template>
    </Column>
    <Column
      field="totalExperience"
      header="Total Experience"
      class="min-w-52"
      v-if="showAllColumns"
    >
      <template #body="{ data }"> {{ data.totalExperience }} </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by total experience"
        />
      </template>
    </Column>
    <Column
      field="education"
      header="Education"
      class="max-w-52 truncate text-nowrap"
      v-if="showAllColumns"
    >
      <template #body="{ data }"> {{ data.education }} </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter"
          placeholder="Search by education"
        />
      </template>
    </Column>
    <Column field="currentCtc" header="Current CTC" class="min-w-52" v-if="showAllColumns">
      <template #body="{ data }"> {{ data.currentCtc }} </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by current CTC"
        />
      </template>
    </Column>
    <Column field="pan" header="Pan" class="min-w-52">
      <template #body="{ data }"> {{ data.pan }} </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by pan"
        />
      </template>
    </Column>
    <Column field="createdOn" header="Created On" dataType="date" class="min-w-40">
      <template #body="{ data }">
        {{ formatDate(data.createdAt) }}
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="date"
          class="p-column-filter"
          placeholder="Search by approved date"
        />
      </template>
    </Column>
  </DataTable>

  <CandidateModal
    v-if="candidateToEdit"
    :candidate="candidateToEdit"
    :visible="editCandidateModal"
    @close="
      editCandidateModal = false;
      candidateToEdit = undefined;
    "
    :isUpdate="true"
    @update="(candidateForm) => (candidateToEdit = candidateForm)"
    @save="update(candidateToEdit)"
  />
</template>
