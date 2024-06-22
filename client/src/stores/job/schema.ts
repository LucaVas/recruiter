import type { Client } from '../client/schema';
import type { Questionnaire } from '../questionnaire/schema';
import type { Skill } from '../skill/schema';

export interface JobI {
  client: Client;
  name: string;
  status: JobStatus;
  wantedCvs: number;
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
  skills: Skill[];
  questionnaire: Questionnaire;
}

export type Job = JobI & {
  id: number;
  numberOfCandidates: number | null;
  createdAt: Date;
  updatedAt: Date;
};
export type NewJob = JobI;

export type ContractType = 'PERMANENT' | 'TEMPORARY';
export type Currency = 'INR';
export type JobStatus = 'OPEN' | 'NO_CV_ACCEPTED' | 'CLOSED' | 'ARCHIVED' | 'DELETED';

export type ChangeJobStatusRequest = { status: JobStatus };
export type DeleteJob = { id: number };

// response
export type JobResponse = { id: number; job: Job };
