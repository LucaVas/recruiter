<script setup lang="ts">
import Tag from 'primevue/tag';
import TieredMenu from 'primevue/tieredmenu';
import Button from 'primevue/button';
import Toast from 'primevue/toast';
import type { JobStatus } from '@/stores/job/types';
import { formatStatus } from './utils';
import { formatDate } from '@/utils/dateUtils';
import { ref } from 'vue';
import type { MenuItem } from 'primevue/menuitem';
import { isAdmin } from '../../stores/user/index';
import { deleteJob, changeJobStatus } from '@/stores/job';
import { ApiError } from '../../utils/types';
import DeleteJobModal from '../dashboard-table/DeleteJobModal.vue';
import { useRouter } from 'vue-router';
import { getSeverity, getStatusIcon } from './utils';

const router = useRouter();

const { jobId, status, createdAt } = defineProps<{
  jobId: number;
  status: JobStatus;
  createdAt: string;
}>();
const emits = defineEmits<{
  (e: 'passError', content: string): void;
}>();

const splitButtonChoices = ref([
  {
    label: 'Open Job',
    icon: 'pi pi-lock-open',
    condition: status !== 'OPEN',
    command: async () => {
      await changeJobStatus(jobId, 'OPEN');
      router.go(0);
    },
  },
  {
    label: 'No CV Accepted',
    icon: 'pi pi-lock',
    condition: status !== 'NO_CV_ACCEPTED',
    command: async () => {
      await changeJobStatus(jobId, 'NO_CV_ACCEPTED');
      router.go(0);
    },
  },
  {
    label: 'Close Job',
    icon: 'pi pi-times-circle',
    condition: status !== 'CLOSED',
    command: async () => {
      await changeJobStatus(jobId, 'CLOSED');
      router.go(0);
    },
  },
  {
    label: 'Delete',
    icon: 'pi pi-trash',
    condition: isAdmin.value,
    command: () => {
      deleteJobModalOpen.value = true;
    },
  },
]);

const getSplitButtonChoices = function (): MenuItem[] {
  return splitButtonChoices.value.filter((choice) => choice.condition);
};

const deleteJobModalOpen = ref(false);

async function delJob(id: number) {
  try {
    await deleteJob(id);
    router.go(0);
  } catch (err) {
    if (err instanceof ApiError) emits('passError', err.message);
  } finally {
    deleteJobModalOpen.value = false;
  }
}

const toggle = (event) => {
  menu.value.toggle(event);
};
const menu = ref();
</script>

<template>
  <Toast />
  <div class="flex flex-col items-start justify-between md:flex-row md:items-center">
    <div class="flex w-full flex-row items-center justify-between gap-2 md:justify-normal">
      <Tag
        :icon="getStatusIcon(status)"
        :severity="getSeverity(status)"
        class="h-10 min-w-fit px-4"
        :value="formatStatus(status)"
      />
      <div class="card flex justify-center">
        <DeleteJobModal
          :visible="deleteJobModalOpen"
          @closeModal="deleteJobModalOpen = false"
          @deleteJob="delJob(jobId)"
        />
        <Button
          type="button"
          size="small"
          label="Change Status"
          v-if="status !== 'ARCHIVED'"
          @click="toggle"
          aria-haspopup="true"
          aria-controls="overlay_tmenu"
          class="h-10 min-w-fit"
        />
        <TieredMenu ref="menu" id="overlay_tmenu" :model="getSplitButtonChoices()" popup />
      </div>
    </div>
    <p class="min-w-fit">Created on: {{ formatDate(createdAt) }}</p>
  </div>
</template>
