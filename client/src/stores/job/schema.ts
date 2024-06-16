import type { Client } from '../client/schema';
import type { NewQuestionnaire, Questionnaire } from '../questionnaire/schema';
import type { NewSkill, Skill } from '../skill/schema';

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
}

export type Job = JobI & {
  id: number;
  numberOfCandidates: number | null;
  skills: Skill[];
  questionnaire: Questionnaire;
  createdAt: Date;
  updatedAt: Date;
};
export type NewJob = JobI & { skills: NewSkill[]; questionnaire: NewQuestionnaire };

export type ContractType = 'PERMANENT' | 'TEMPORARY';
export type Currency = 'INR';
export type JobStatus = 'OPEN' | 'NO_CV_ACCEPTED' | 'CLOSED' | 'ARCHIVED' | 'DELETED';

export type ChangeJobStatusRequest = { status: JobStatus };
export type DeleteJob = { id: number };

// response
export type JobResponse = { id: number; job: Job };
