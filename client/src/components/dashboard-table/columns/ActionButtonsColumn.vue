<script setup lang="ts">
import DeleteJobModal from '../DeleteJobModal.vue';
import SplitButton from 'primevue/splitbutton';
import Button from 'primevue/button';
import { ref } from 'vue';
import { deleteJob } from '@/stores/job/index';
import type { MenuItem } from 'primevue/menuitem';
import { ApiError } from '../../../utils/types';
import { role } from '@/stores/user';
import { useRouter } from 'vue-router';

const router = useRouter();
const { data } = defineProps<{
  data: any;
}>();

const emits = defineEmits<{
  (e: 'reloadTable'): void;
  (e: 'passError', message: string): void;
}>();

const isAdmin = ref(role.value === 'ROLE_ADMIN');

const getSplitButtonChoices = function (): MenuItem[] {
  return [
    {
      label: 'Delete',
      icon: 'pi pi-times',
      command: async () => {
        deleteJobModalOpen.value = true;
      },
    },
  ];
};
const deleteJobModalOpen = ref(false);

async function delJob(id: number) {
  try {
    await deleteJob(id);
    emits('reloadTable');
  } catch (err) {
    if (err instanceof ApiError) emits('passError', err.message);
  } finally {
    deleteJobModalOpen.value = false;
  }
}
const applyToJob = (jobId: number): void => {
  router.push({ name: 'NewCandidacy', params: { jobId: jobId } });
};
</script>

<template>
  <div>
    <DeleteJobModal
      v-if="isAdmin"
      :visible="deleteJobModalOpen"
      @closeModal="deleteJobModalOpen = false"
      @deleteJob="delJob(data.id)"
    />

    <SplitButton
      v-if="isAdmin"
      label="Edit"
      size="small"
      class="p-2"
      outlined
      severity="contrast"
      menuButtonIcon="pi pi-angle-down"
      @click="applyToJob(data.id)"
      :model="getSplitButtonChoices()"
    />

    <Button
      v-else
      label="Apply"
      size="small"
      class="p-2"
      outlined
      severity="contrast"
      @click="applyToJob(data.id)"
    />
  </div>
</template>
