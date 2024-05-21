import { ref } from 'vue';
import { getJob } from '@/stores/job/index';
import type { Job } from '@/stores/job/schema';

export const loading = ref(false);
export const job = ref<Job>();

// modal
export const modalOpen = ref(false);

export const getJobDetails = async (id: number) => {
  loading.value = true;
  return await getJob(id);
};
