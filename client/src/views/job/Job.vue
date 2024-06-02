<script setup lang="ts">
import { onMounted } from 'vue';
import { delJob, deletingJob, deleteJobModalOpen } from './jobCommons';
import { useToast } from 'primevue/usetoast';
import { useRoute, useRouter } from 'vue-router';
import JobTitle from '@/components/job/job-page/JobTitle.vue';
import JobMetadata from '@/components/job/job-page/JobMetadata.vue';
import JobButtons from '@/components/job/job-page/JobButtons.vue';
import JobDescription from '@/components/job/job-page/JobDescription.vue';
import JobSkills from '@/components/job/job-page/JobSkills.vue';
import JobHiringDetailsModal from '@/components/job/job-page/JobHiringDetailsModal.vue';
import DeleteJobModal from '@/components/job/DeleteJobModal.vue';
import { handleError } from '@/utils/errorUtils';
import { ref } from 'vue';
import { getJob } from '@/stores/job/index';
import type { Job } from '@/stores/job/schema';

const loading = ref(false);
const job = ref<Job>();

// modal
const modalOpen = ref(false);

const getJobDetails = async (id: number) => {
  loading.value = true;
  return await getJob(id);
};

// route
const router = useRouter();
const route = useRoute();
const id = Number(route.params.id);

// toast
const toast = useToast();

// functions

onMounted(async () => {
  try {
    job.value = await getJobDetails(id);
  } catch (err) {
    handleError(toast, err);
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
      @deleteJob="delJob(id, router, toast)"
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
