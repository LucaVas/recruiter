import type { ToastServiceMethods } from 'primevue/toastservice';
import type { Router } from 'vue-router';
import { ref } from 'vue';
import { deleteJob, getFullJob } from '@/stores/job';
import { handleError, showSuccess } from '@/utils/errorUtils';
import type { NewSkill } from '@/stores/skill/schema';
import { createSkill } from '@/stores/skill';

export const deleteJobModalOpen = ref(false);
export const deletingJob = ref(false);

export const loadingJob = ref(false);

export const creatingSkill = ref(false);
export const skillModalOpen = ref(false);

export const delJob = async (id: number, router: Router, toast: ToastServiceMethods) => {
  deletingJob.value = true;
  try {
    await deleteJob(id);
    router.push({ name: 'Dashboard' });
    setTimeout(() => {
      showSuccess(toast, 'Job deleted successfully.');
    }, 1000);
  } catch (err) {
    handleError(toast, err);
  } finally {
    deletingJob.value = false;
  }
};

export const loadJobData = async (jobId: number, toast: ToastServiceMethods) => {
  loadingJob.value = true;
  try {
    const job = await getFullJob(jobId);
    return job;
  } catch (err) {
    handleError(toast, err);
  } finally {
    loadingJob.value = false;
  }
};

export const createNewSkill = async (skill: NewSkill, toast: ToastServiceMethods) => {
  creatingSkill.value = true;
  try {
    const newSkill = await createSkill(skill);
    skillModalOpen.value = false;
    return newSkill;
  } catch (err) {
    handleError(toast, err);
  } finally {
    creatingSkill.value = false;
  }
};
