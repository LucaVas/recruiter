import type { NewJob } from '@/stores/job/types';
import type { RawSkillDto } from '@/stores/skill/types';
import { ref } from 'vue';

export const job = ref<NewJobForm>({
  client: '',
  name: '',
  status: 'OPEN',
  contractType: { contractTypeName: 'PERMANENT' },
  wantedCvs: '',
  skills: [],
  noticePeriodInDays: '',
  experienceRangeMin: '',
  experienceRangeMax: '',
  salaryBudget: '',
  currency: 'INR',
  description: '',
  bonusPayPerCv: '',
  closureBonus: '',
  cvRatePaymentDate: new Date(),
  closureBonusPaymentDate: new Date(),
});

export type NewJobForm = Omit<
  NewJob,
  | 'noticePeriodInDays'
  | 'experienceRangeMin'
  | 'experienceRangeMax'
  | 'salaryBudget'
  | 'bonusPayPerCv'
  | 'wantedCvs'
> & {
  noticePeriodInDays: string;
  experienceRangeMin: string;
  experienceRangeMax: string;
  salaryBudget: string;
  bonusPayPerCv: string;
  wantedCvs: string;
};

export function updateNewJobInformation(details: Partial<NewJobForm>) {
  job.value.client = details.client ?? '';
  job.value.name = details.name ?? '';
  job.value.status = details.status ?? 'OPEN';
  job.value.contractType = details.contractType ?? { contractTypeName: 'PERMANENT' };
}

export function updateNewJobHiringDetails(details: Partial<NewJobForm>) {
  job.value.wantedCvs = details.wantedCvs ?? '';
  job.value.noticePeriodInDays = details.noticePeriodInDays ?? '';
  job.value.experienceRangeMin = details.experienceRangeMin ?? '';
  job.value.experienceRangeMax = details.experienceRangeMax ?? '';
  job.value.salaryBudget = details.salaryBudget ?? '';
  job.value.currency = details.currency ?? 'INR';
  job.value.description = details.description ?? '';
}

export function updateNewJobPaymentDetails(details: Partial<NewJobForm>) {
  job.value.bonusPayPerCv = details.bonusPayPerCv ?? '';
  job.value.closureBonus = details.closureBonus ?? '';
  job.value.cvRatePaymentDate = details.cvRatePaymentDate
    ? new Date(details.cvRatePaymentDate)
    : new Date();
  job.value.closureBonusPaymentDate = details.closureBonusPaymentDate
    ? new Date(details.closureBonusPaymentDate)
    : new Date();
}

export function updateNewJobSkills(skills: RawSkillDto[]): void {
  job.value.skills = skills;
}
