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
import { getFullJob } from '@/stores/job/index';
import type { FullJob } from '@/stores/job/schema';
import JobQuestionnairePanel from '@/components/job/job-page/JobQuestionnairePanel.vue';

const loading = ref(false);
const job = ref<FullJob>();

// modal
const modalOpen = ref(false);

const getJobDetails = async (id: number) => {
  loading.value = true;
  try {
    job.value = await getFullJob(id);
  } catch (err) {
    handleError(toast, err);
    setTimeout(() => {
      router.push({ name: 'Dashboard' });
    }, 3000);
  } finally {
    loading.value = false;
  }
};

// route
const router = useRouter();
const route = useRoute();
const id = Number(route.params.id);

// toast
const toast = useToast();

// functions

onMounted(async () => {
  await getJobDetails(id);
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
    <JobQuestionnairePanel :questionnaire="job.questionnaire" />
  </div>
  <ProgressSpinner v-else />
</template>
