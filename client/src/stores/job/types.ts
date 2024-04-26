import type { Skill } from '../skill/types';

// backend dtos
export type Job = {
  id: number;
  client: string;
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
  comments: string;
  numberOfCandidates: number | null;
  closureBonusPaymentDate: Date;
  cvRatePaymentDate: Date;
  createdAt: string;
};
export type ContractType = {
  contractTypeName: ContractTypeName;
};

// backend domain objects
export type ContractTypeName = 'PERMANENT' | 'TEMPORARY';
export type Currency = 'INR';
export type JobStatus = 'OPEN' | 'NO_CV_ACCEPTED' | 'CLOSED' | 'ARCHIVED';
export type JobResponse = { id: number; job: Job };
export type NewJobRequest = Omit<Job, 'id' | 'comments' | 'numberOfCandidates' | 'createdAt'>;
export type UpdateJobRequest = NewJobRequest & { id: number };
export type ChangeJobStatusRequest = { jobStatus: JobStatus };
