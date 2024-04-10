import type { NewJob } from '@/stores/job/types';
import { ref } from 'vue';

export const job = ref<NewJob>({
  client: '',
  name: '',
  status: 'OPEN',
  contractType: 'PERMANENT',
  wantedCvs: 0,
  noticePeriodInDays: 0,
  experienceRangeMin: 0,
  experienceRangeMax: 45,
  salaryBudget: 0,
  currency: 'INR',
  description: '',
  bonusPayPerCv: 0,
  closureBonus: '',
  cvRatePaymentDate: new Date(),
  closureBonusPaymentDate: new Date(),
});

export function updateNewJobInformation(details) {
  job.value.client = details.client;
  job.value.name = details.name;
  job.value.status = details.status;
  job.value.contractType = details.contractType;
}

export function updateNewJobHiringDetails(details) {
  job.value.wantedCvs = details.wantedCvs;
  job.value.noticePeriodInDays = details.noticePeriodInDays;
  job.value.experienceRangeMin = details.experienceRangeMin;
  job.value.experienceRangeMax = details.experienceRangeMax;
  job.value.salaryBudget = details.salaryBudget;
  job.value.currency = details.currency;
  job.value.description = details.description;
}

export function updateNewJobPaymentDetails(details) {
  job.value.bonusPayPerCv = details.bonusPayPerCv;
  job.value.closureBonus = details.closureBonus;
  job.value.cvRatePaymentDate = new Date(details.cvRatePaymentDate);
  job.value.closureBonusPaymentDate = new Date(details.closureBonusPaymentDate);
}
