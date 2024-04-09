<script setup lang="ts">
import { onMounted, ref } from 'vue';
import Toast from 'primevue/toast';
import { getJobDetails } from './index';
import { useToast } from 'primevue/usetoast';
import { useRoute } from 'vue-router';
import type { JobDto } from '@/stores/job/types';
import JobTitle from '@/components/job/JobTitle.vue';
import JobMetadata from '@/components/job/JobMetadata.vue';
import JobButtons from '@/components/job/JobButtons.vue';
import JobDescription from '@/components/job/JobDescription.vue';
import JobSkills from '@/components/job/JobSkills.vue';
import JobHiringDetailsModal from '@/components/job/JobHiringDetailsModal.vue';

// toast
const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};

// job details
const job = ref<JobDto>();

// modal
const modalOpen = ref(false);

onMounted(async () => {
  const route = useRoute();
  const id = route.params.id;
  try {
    job.value = await getJobDetails(Number(id));
  } catch (e) {
    showError(e as string);
  }
});
</script>

<template>
  <Toast />
  <div v-if="job" class="flex w-full flex-col items-start gap-4">
    <JobTitle :title="job.name" />
    <JobMetadata
      :client="job.client"
      :contractType="job.contractType.contractTypeName"
      :wantedCvs="job.wantedCvs"
      :candidates="job.numberOfCandidates"
      :createdOn="job.createdAt"
      :status="job.status"
    />
    <JobHiringDetailsModal
      :visible="modalOpen"
      @close="modalOpen = false"
      :experienceRangeMin="job.experienceRangeMin"
      :experienceRangeMax="job.experienceRangeMax"
      :noticePeriodInDays="job.noticePeriodInDays"
      :salaryBudget="job.salaryBudget"
      :currency="job.currency"
      :bonusPayPerCv="job.bonusPayPerCv"
      :closureBonus="job.closureBonus"
      :closureBonusPaymentDate="job.closureBonusPaymentDate"
      :cvRatePaymentDate="job.cvRatePaymentDate"
    />
    <JobButtons @openModal="modalOpen = true" :id="job.id" :status="job.status" />
    <JobDescription :description="job.description" />
    <JobSkills :skills="job.skills" />
  </div>
</template>
