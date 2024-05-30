import type { Client } from '@/stores/client/schema';
import type { Job, JobStatus } from '@/stores/job/schema';
import { getAllSkills } from '@/stores/skill';
import type { Skill } from '@/stores/skill/schema';
import { handleError, showSuccess } from '@/utils/errorUtils';
import type { ToastServiceMethods } from 'primevue/toastservice';
import { ref } from 'vue';
import { loadJobData } from '../jobCommons';
import { getAllClients } from '@/stores/client';
import { changeJobStatus, updateJob } from '@/stores/job';
import type { Questionnaire } from '@/stores/question/schema';

export const job = ref<Job>({
  id: 0,
  client: {} as Client,
  name: '',
  status: 'OPEN',
  contractType: 'TEMPORARY',
  wantedCvs: 0,
  experienceRangeMin: 0,
  experienceRangeMax: 0,
  noticePeriodInDays: 0,
  skills: [],
  salaryBudget: 0,
  currency: 'INR',
  description: '',
  bonusPayPerCv: 0,
  closureBonus: 'Not Applicable',
  numberOfCandidates: null,
  questionnaire: {} as Questionnaire,
  closureBonusPaymentDate: new Date(),
  cvRatePaymentDate: new Date(),
  createdDTime: new Date(),
  modifiedDTime: new Date(),
});
export const clients = ref<Client[]>([]);
export const skills = ref<Skill[]>([]);
export const loading = ref(false);

export const initializingJob = ref(false);
export const updatingJob = ref(false);
export const jobUpdated = ref(false);
export const changingStatus = ref(false);

export const initializeJob = async (jobId: number, toast: ToastServiceMethods) => {
  initializingJob.value = true;
  try {
    const [d, c, s] = await Promise.all([
      loadJobData(jobId, toast),
      getAllClients(),
      getAllSkills(),
    ]);
    job.value = d;
    clients.value = c;
    skills.value = s;
  } catch (err) {
    handleError(toast, err);
  } finally {
    initializingJob.value = false;
  }
};

export const update = async (job: Job | undefined, toast: ToastServiceMethods) => {
  if (!job) return;
  updatingJob.value = true;
  try {
    await updateJob(job);
    jobUpdated.value = true;
  } catch (err) {
    handleError(toast, err);
  } finally {
    updatingJob.value = false;
  }
};

export const changeStatus = async (id: number, status: JobStatus, toast: ToastServiceMethods) => {
  changingStatus.value = true;
  try {
    await changeJobStatus(id, status);
    showSuccess(toast, 'Job status changed successfully.');
    await initializeJob(id, toast);
  } catch (err) {
    handleError(toast, err);
  } finally {
    changingStatus.value = false;
  }
};

export const addSkill = (job: Job | undefined, skill: Skill | undefined): void => {
  if (!job || !skill) return;
  if (job.skills.some((s: Skill) => s.name === skill.name)) return;
  job.skills.unshift(skill);
};

export const removeSkill = (job: Job | undefined, skill: Skill): void => {
  if (!job) return;
  if (!job.skills.includes(skill)) return;
  job.skills.splice(job.skills.indexOf(skill), 1);
};
