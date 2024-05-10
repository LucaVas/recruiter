import type { Client } from '@/stores/client/schema';
import type { NewJobRequest } from '@/stores/job/schema';
import type { Skill } from '@/stores/skill/schema';
import { ref } from 'vue';

export const job = ref<NewJobRequest>({
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
  closureBonus: 0,
  questions: [],
  closureBonusPaymentDate: new Date(),
  cvRatePaymentDate: new Date(),
});

export const removeSkill = (job: NewJobRequest, skill: Skill): void => {
  if (!job.skills.includes(skill)) return;
  job.skills.splice(job.skills.indexOf(skill), 1);
};

export const addSkill = (job: NewJobRequest, skill: Skill): void => {
  if (job.skills.some((s: Skill) => s.name === skill.name)) return;
  job.skills.unshift(skill);
};

export const jobCreated = ref(false);
export const creatingJob = ref(false);
export const openQuestionModal = ref(false);
export const openSkillModal = ref(false)
export const openQuestionSearchModal = ref(false);
export const clients = ref<Client[]>([]);
export const skills = ref<Skill[]>([]);
