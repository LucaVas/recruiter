import type { Client } from '../client/schema';
import type { NewQuestionnaire } from '../questionnaire/schema';
import type { Skill } from '../skill/schema';

export interface JobI {
  client: Client;
  name: string;
  status: JobStatus;
  wantedCvs: number;
  skills: Skill[];
  contractType: ContractType;
  experienceRangeMin: number;
  experienceRangeMax: number;
  noticePeriodInDays: number;
  salaryBudget: number;
  currency: Currency;
  description: string;
  bonusPayPerCv: number;
  closureBonus: string;
  closureBonusPaymentDate: Date;
  cvRatePaymentDate: Date;
  questionnaire: NewQuestionnaire;
}

export type Job = JobI & {
  id: number;
  createdAt: Date;
  updatedAt: Date;
  numberOfCandidates: number | null;
};
export type NewJob = JobI;

export type ContractType = 'PERMANENT' | 'TEMPORARY';
export type Currency = 'INR';
export type JobStatus = 'OPEN' | 'NO_CV_ACCEPTED' | 'CLOSED' | 'ARCHIVED' | 'DELETED';

export type ChangeJobStatusRequest = { status: JobStatus };
export type DeleteJob = { id: number };

// response
export type JobResponse = { id: number; job: Job };
