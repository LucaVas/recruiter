<template>
  <Toast />
  <CommentsModal
    :comments="comments"
    :visible="openCommentsHistoryModal"
    @close="openCommentsHistoryModal = false"
  />
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
      <CandidaciesTableHeader :filters="filters" @clearFilter="clearFilter()" />
    </template>
    <template #empty> No candidacies found. </template>
    <template #loading> Loading candidacies, please wait... </template>

    <Column field="action" header="" class="min-w-fit">
      <template #body="{ data }">
        <CandidaciesTableActionButtonsColumn
          :data="data"
          @seeCommentsHistory="
            {
              openCommentsHistoryModal = true;
              getComments(data.job.id, data.candidate.pan);
            }
          "
        />
      </template>
    </Column>
    <Column header="Candidate" class="min-w-52">
      <template #body="{ data }">
        <CandidacyCandidateCard :candidate="data.candidate" />
      </template>
    </Column>
    <Column header="Job" class="min-w-52">
      <template #body="{ data }">
        <CandidacyJobCard :candidacy="data" />
      </template>
    </Column>
    <Column header="Status" class="min-w-52">
      <template #body="{ data }">
        <Tag
          :severity="getCandidacyStatusSeverity(data.status)"
          :value="getCandidacyStatus(data.status)"
        ></Tag>
      </template>
    </Column>
  </DataTable>
</template>

<script setup lang="ts">
import { getAllCandidacies, getCandidacyComments } from '@/stores/candidacy';
import type { Candidacy } from '@/stores/candidacy/schema';
import { ApiError } from '@/utils/types';
import Column from 'primevue/column';
import DataTable from 'primevue/datatable';
import Toast from 'primevue/toast';
import Tag from 'primevue/tag';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';
import { columns } from '.';
import CandidaciesTableHeader from './CandidaciesTableHeader.vue';
import CandidaciesTableActionButtonsColumn from './CandidaciesTableActionButtonsColumn.vue';
import CandidacyCandidateCard from './CandidacyCandidateCard.vue';
import CandidacyJobCard from './CandidacyJobCard.vue';
import CommentsModal from './CommentsModal.vue';
import { clearFilter, filters, initFilters } from './filters';
import { getCandidacyStatus, getCandidacyStatusSeverity } from './utils';
import { type CandidacyComment } from '@/stores/candidacy/schema';

const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

const loadingTable = ref(false);
const loadingComments = ref(false);
const candidates = ref<Candidacy[]>();
const openCommentsHistoryModal = ref(false);
const comments = ref<CandidacyComment[]>([]);

const getComments = async (jobId: number, pan: string) => {
  loadingComments.value = true;
  try {
    comments.value = await getCandidacyComments(jobId, pan);
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
  } finally {
    loadingComments.value = false;
  }
};
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
