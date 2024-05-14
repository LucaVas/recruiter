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
      <CandidaciesTableHeader :filters="filters" @clearFilter="clearFilter()" />
    </template>
    <template #empty> No candidacies found. </template>
    <template #loading> Loading candidacies, please wait... </template>

    <Column field="action" header="" class="min-w-fit">
      <template #body="{ data }">
        <CommentsModal
          :loadingComments="loadingComments"
          :sendingComment="sendingComment"
          :comments="comments"
          :visible="openCommentsHistoryModal"
          @send="(comment) => send(data.job.id, data.candidate.pan, comment)"
          @close="openCommentsHistoryModal = false"
        />
        <DeleteCandidacyModal
          :visible="deleteCandidacyModalOpen"
          @close="deleteCandidacyModalOpen = false"
          @delete="delCandidacy(data.job.id, data.candidate.pan)"
          :deletingCandidacy="deletingCandidacy"
        />
        <CandidacyFilesModal
          :files="candidacyFiles"
          :visible="candidacyFilesModalOpen"
          :loading="loadingFiles"
          @close="candidacyFilesModalOpen = false"
        />
        <CandidaciesTableActionButtonsColumn
          :data="data"
          @seeComments="
            {
              openCommentsHistoryModal = true;
              getComments(data.job.id, data.candidate.pan);
            }
          "
          @delete="deleteCandidacyModalOpen = true"
          @seeFiles="getFiles(data.job.id, data.candidate.pan)"
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
import {
  addCandidacyComment,
  getAllCandidacies,
  getCandidacyComments,
  deleteCandidacy,
} from '@/stores/candidacy';
import type { Candidacy, CandidacyFile } from '@/stores/candidacy/schema';
import { ApiError } from '@/utils/types';
import Column from 'primevue/column';
import DataTable from 'primevue/datatable';
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
import { showError, showSuccess } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import DeleteCandidacyModal from '@/components/candidacy/DeleteCandidacyModal.vue';
import type CandidacyFilesModal from '@/components/candidacy/files/CandidacyFilesModal.vue';
import { getCandidacyFiles } from '../../../stores/candidacy/index';

const toast = useToast();

const deleteCandidacyModalOpen = ref(false);
const deletingCandidacy = ref(false);
const candidacyFilesModalOpen = ref(false);
const loadingFiles = ref(false);
const loadingTable = ref(false);
const sendingComment = ref(false);
const loadingComments = ref(false);
const candidates = ref<Candidacy[]>();
const openCommentsHistoryModal = ref(false);
const comments = ref<CandidacyComment[]>([]);
const candidacyFiles = ref<CandidacyFile[]>([]);

const send = async (jobId: number, pan: string, comment: string) => {
  sendingComment.value = true;
  try {
    await addCandidacyComment(jobId, pan, { text: comment });
    await getComments(jobId, pan);
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    sendingComment.value = false;
  }
};

const getComments = async (jobId: number, pan: string) => {
  loadingComments.value = true;
  try {
    comments.value = await getCandidacyComments(jobId, pan);
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    loadingComments.value = false;
  }
};

const delCandidacy = async (jobId: number, pan: string) => {
  deletingCandidacy.value = true;
  try {
    await deleteCandidacy(jobId, pan);
    deleteCandidacyModalOpen.value = false;
    showSuccess(toast, 'Candidacy deleted successfully');
    await initTable();
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    deletingCandidacy.value = false;
  }
};

const getFiles = async (jobId: number, pan: string) => {
  loadingFiles.value = true;
  try {
    candidacyFilesModalOpen.value = true;
    candidacyFiles.value = await getCandidacyFiles(jobId, pan);
    loadingFiles.value = false;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    loadingFiles.value = false;
  }
};

async function initTable() {
  loadingTable.value = true;
  try {
    candidates.value = await getAllCandidacies();
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    loadingTable.value = false;
  }
}
initFilters();
onMounted(async () => {
  await initTable();
});
</script>
