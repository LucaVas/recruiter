<template>
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
        />
        <TieredMenu ref="menu" id="overlay_tmenu" :model="getSplitButtonChoices()" popup />
      </div>
    </div>
    <p class="min-w-fit">Created on: {{ formatDate(createdAt) }}</p>
  </div>
</template>

<script setup lang="ts">
import Tag from 'primevue/tag';
import TieredMenu from 'primevue/tieredmenu';
import Button from 'primevue/button';
import type { JobStatus } from '@/stores/job/schema';
import { formatDate } from '@/utils/dateUtils';
import { ref } from 'vue';
import type { MenuItem } from 'primevue/menuitem';
import { isAdmin } from '@/stores/auth';
import DeleteJobModal from '@/components/job/shared/DeleteJobModal.vue';
import { getSeverity, getStatusIcon, formatStatus } from '../shared/utils';

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
    condition: status !== 'OPEN',
    command: async () => {
      emits('changeStatus', 'OPEN');
    },
  },
  {
    label: 'No CV Accepted',
    icon: 'pi pi-lock',
    condition: status !== 'NO_CV_ACCEPTED',
    command: async () => {
      emits('changeStatus', 'NO_CV_ACCEPTED');
    },
  },
  {
    label: 'Close Job',
    icon: 'pi pi-times-circle',
    condition: status !== 'CLOSED',
    command: async () => {
      emits('changeStatus', 'CLOSED');
    },
  },
  {
    label: 'Archive Job',
    icon: 'pi pi-folder',
    condition: status !== 'ARCHIVED',
    command: async () => {
      emits('changeStatus', 'ARCHIVED');
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
</script>