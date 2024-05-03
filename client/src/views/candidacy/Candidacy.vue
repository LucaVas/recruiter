<script setup lang="ts">
import { onMounted, ref } from 'vue';
import Toast from 'primevue/toast';
import { getJobDetails } from './index';
import { useToast } from 'primevue/usetoast';
import { useRoute, useRouter } from 'vue-router';
import type { Job } from '@/stores/job/schema';
import JobTitle from '@/components/job/job-page/JobTitle.vue';
import JobMetadata from '@/components/job/job-page/JobMetadata.vue';
import JobButtons from '@/components/job/job-page/JobButtons.vue';
import JobDescription from '@/components/job/job-page/JobDescription.vue';
import JobSkills from '@/components/job/job-page/JobSkills.vue';
import JobHiringDetailsModal from '@/components/job/job-page/JobHiringDetailsModal.vue';
import { deleteJob } from '@/stores/job';
import DeleteJobModal from '@/components/job/shared/DeleteJobModal.vue';
import { Candidacy } from '../../stores/candidacy/schema';
import { getCandidacy } from '@/stores/candidacy/index';

// constants
const deletingJob = ref(false);

// route
const router = useRouter();
const route = useRoute();
const jobId = Number(route.params.jobId);
const pan = Number(route.params.pan);

// toast
const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};
const showSuccess = (content: string) => {
  toast.add({ severity: 'success', summary: 'Success', detail: content, life: 5000 });
};

const job = ref<Job>();
const candidacy = ref<Candidacy>();

// modal
const modalOpen = ref(false);
const deleteJobModalOpen = ref(false);

// functions
const delJob = async (id: number) => {
  deletingJob.value = true;
  try {
    await deleteJob(id);
    showSuccess('Job deleted successfully.');
    setTimeout(() => {
      router.go(0);
    }, 2000);
  } catch (e) {
    showError(e as string);
  } finally {
    deletingJob.value = false;
  }
};

onMounted(async () => {
  try {
    job.value = await getJobDetails(jobId);
    const res = await getCandidacy(jobId, pan);
  } catch (e) {
    showError(e as string);
  }
});
</script>

<template>
  <Toast />
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
</template>
