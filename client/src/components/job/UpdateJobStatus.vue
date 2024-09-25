<script setup lang="ts">
import Tag from 'primevue/tag';
import TieredMenu from 'primevue/tieredmenu';
import Button from 'primevue/button';
import type { JobStatus } from '@/stores/job/schema';
import { formatDate } from '@/utils/dateUtils';
import { ref } from 'vue';
import DeleteJobModal from '../modals/DeleteJobModal.vue';
import { getJobSeverity, getJobStatusIcon, formatJobStatus } from './utils';

// props
const { status, createdAt } = defineProps<{
  status: JobStatus;
  createdAt: Date;
}>();

// emits
const emits = defineEmits<{
  (e: 'changeStatus', status: JobStatus): void;
  (e: 'delete'): void;
  (e: 'passError', content: string): void;
}>();

// variables
const deleteJobModalOpen = ref(false);
const menu = ref();

const toggle = (event: MouseEvent) => {
  menu.value.toggle(event);
};

const splitButtonChoices = ref([
  {
    label: 'Open Job',
    icon: 'pi pi-lock-open',
    command: async () => {
      emits('changeStatus', 'OPEN');
    },
  },
  {
    label: 'No CV Accepted',
    icon: 'pi pi-lock',
    command: async () => {
      emits('changeStatus', 'NO_CV_ACCEPTED');
    },
  },
  {
    label: 'Close Job',
    icon: 'pi pi-times-circle',
    command: async () => {
      emits('changeStatus', 'CLOSED');
    },
  },
  {
    label: 'Archive Job',
    icon: 'pi pi-folder',
    command: async () => {
      emits('changeStatus', 'ARCHIVED');
    },
  },
  {
    label: 'Delete',
    icon: 'pi pi-trash',
    command: () => {
      deleteJobModalOpen.value = true;
    },
  },
]);
</script>

<template>
  <div class="flex flex-col items-start justify-between gap-3 md:flex-row md:items-center">
    <div class="flex w-full flex-row items-center justify-between gap-2 md:justify-normal">
      <Tag
        :icon="getJobStatusIcon(status)"
        :severity="getJobSeverity(status)"
        class="h-10 min-w-fit px-4"
        :value="formatJobStatus(status)"
      />
      <div class="card flex justify-center">
        <DeleteJobModal
          :visible="deleteJobModalOpen"
          @closeModal="deleteJobModalOpen = false"
          @deleteJob="$emit('delete')"
        />
        <Button
          type="button"
          size="small"
          label="Change Status"
          v-if="status !== 'DELETED'"
          @click="toggle"
          aria-haspopup="true"
          aria-controls="overlay_tmenu"
          class="h-10 min-w-fit"
          outlined
        />
        <TieredMenu ref="menu" id="overlay_tmenu" :model="splitButtonChoices" popup />
      </div>
    </div>
    <p class="min-w-fit">Created on: {{ formatDate(createdAt) }}</p>
  </div>
</template>
