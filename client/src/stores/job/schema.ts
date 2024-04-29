import type { Client } from '../client/schema';
import type { Skill } from '../skill/schema';

// backend dtos
export type Job = {
  id: number;
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
  closureBonus: number;
  closureBonusPaymentDate: Date;
  cvRatePaymentDate: Date;
  numberOfCandidates: number | null;
  createdDTime: Date;
  modifiedDTime: Date;
};

export type ContractType = 'PERMANENT' | 'TEMPORARY';
export type Currency = 'INR';
export type JobStatus = 'OPEN' | 'NO_CV_ACCEPTED' | 'CLOSED' | 'ARCHIVED';

// backend domain objects
export type NewJobRequest = Omit<
  Job,
  'id' | 'comments' | 'numberOfCandidates' | 'createdDTime' | 'modifiedDTime'
>;
export type UpdateJobRequest = NewJobRequest;
export type ChangeJobStatusRequest = { status: JobStatus };
export type DeleteJob = { id: number };

// response
export type JobResponse = { id: number; job: Job };
