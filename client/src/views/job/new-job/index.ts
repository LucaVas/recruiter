import { getAllClients } from '@/stores/client';
import type { Client } from '@/stores/client/schema';
import { createJob } from '@/stores/job';
import type { NewJobRequest } from '@/stores/job/schema';
import { createQuestion } from '@/stores/question';
import type { QuestionForm } from '@/stores/question/schema';
import { getAllSkills } from '@/stores/skill';
import type { Skill } from '@/stores/skill/schema';
import { handleError } from '@/utils/errorUtils';
import { capitalizeText, capitalizeWords } from '@/utils/stringUtils';
import type { ToastServiceMethods } from 'primevue/toastservice';
import { capitalize, ref } from 'vue';

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
  closureBonus: 'Not Applicable',
  questions: [],
  closureBonusPaymentDate: new Date(),
  cvRatePaymentDate: new Date(),
});

export const removeSkill = (job: NewJobRequest, skill: Skill): void => {
  if (!job.skills.includes(skill)) return;
  job.skills.splice(job.skills.indexOf(skill), 1);
};

export const addSkill = (job: NewJobRequest, skill: Skill | undefined): void => {
  if (!job || !skill) return;
  if (job.skills.some((s: Skill) => s.name === skill.name)) return;
  job.skills.unshift(skill);
};

export const jobCreated = ref(false);
export const creatingJob = ref(false);

export const openQuestionModal = ref(false);
export const openQuestionSearchModal = ref(false);
export const clients = ref<Client[]>([]);
export const skills = ref<Skill[]>([]);

export const create = async (job: NewJobRequest, toast: ToastServiceMethods): Promise<void> => {
  creatingJob.value = true;
  try {
    await createJob(job);
    jobCreated.value = true;
  } catch (err) {
    handleError(toast, err);
  } finally {
    creatingJob.value = false;
  }
};

export const loadClients = async (toast: ToastServiceMethods) => {
  try {
    clients.value = await getAllClients();
  } catch (err) {
    handleError(toast, err);
  }
};

export const loadSkills = async (toast: ToastServiceMethods) => {
  try {
    skills.value = await getAllSkills();
  } catch (err) {
    handleError(toast, err);
  }
};

export const createAndAddQuestion = async (
  question: QuestionForm,
  toast: ToastServiceMethods
): Promise<void> => {
  try {
    const newQuestion = await createQuestion({
      ...question,
      text: capitalizeText(question.text),
      title: capitalizeWords(question.title),
      answer: capitalizeText(question.answer),
      division: capitalize(question.division),
      skillNames: question.skillNames.map((s) => capitalize(s)),
    });
    job.value.questions.push(newQuestion);
    skills.value = await getAllSkills();
  } catch (err) {
    handleError(toast, err);
  }
};
