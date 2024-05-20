<script setup lang="ts">
import { onMounted } from 'vue';
import { getJobDetails, deletingJob, job, modalOpen, deleteJobModalOpen } from './index';
import { useToast } from 'primevue/usetoast';
import { useRoute, useRouter } from 'vue-router';
import JobTitle from '@/components/job/job-page/JobTitle.vue';
import JobMetadata from '@/components/job/job-page/JobMetadata.vue';
import JobButtons from '@/components/job/job-page/JobButtons.vue';
import JobDescription from '@/components/job/job-page/JobDescription.vue';
import JobSkills from '@/components/job/job-page/JobSkills.vue';
import JobHiringDetailsModal from '@/components/job/job-page/JobHiringDetailsModal.vue';
import { deleteJob } from '@/stores/job';
import DeleteJobModal from '@/components/job/shared/DeleteJobModal.vue';
import { showError, showSuccess } from '@/utils/errorUtils';
import { ApiError } from '@/utils/types';
import { DEFAULT_SERVER_ERROR } from '@/consts';

// route
const router = useRouter();
const route = useRoute();
const id = Number(route.params.id);

// toast
const toast = useToast();

// functions
const delJob = async (id: number) => {
  deletingJob.value = true;
  try {
    await deleteJob(id);
    deleteJobModalOpen.value = false;
    showSuccess(toast, 'Job deleted successfully.');
    setTimeout(() => {
      router.push({ name: 'Dashboard' });
    }, 1500);
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    deletingJob.value = false;
  }
};

onMounted(async () => {
  try {
    job.value = await getJobDetails(id);
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
    setTimeout(() => {
      router.push({ name: 'Dashboard' });
    }, 3000);
  }
});
</script>

<template>
  <div v-if="job" class="flex w-full flex-col items-start gap-4">
    <JobTitle :title="job.name" />
    <JobMetadata :job="job" />
    <JobHiringDetailsModal :visible="modalOpen" @close="modalOpen = false" :job="job" />
    <DeleteJobModal
      :deleting="deletingJob"
      :visible="deleteJobModalOpen"
      @closeModal="deleteJobModalOpen = false"
      @deleteJob="delJob(id)"
    />
    <JobButtons
      @deleteJob="(id) => (deleteJobModalOpen = true)"
      @openModal="modalOpen = true"
      :id="job.id"
      :status="job.status"
    />
    <JobDescription :description="job.description" />
    <h3 class="text-lg font-medium">Skills</h3>

    <JobSkills :isNewJob="false" :skills="job.skills" />
  </div>
  <ProgressSpinner v-else />
</template>
