import { ref } from 'vue';
import { getJob } from '@/stores/job/index';

export const loading = ref(false);

export const getJobDetails = async (id: number) => {
  loading.value = true;
  return await getJob(id);
};
