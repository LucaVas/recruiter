import type { RawSkill, Skill } from '../skill/types';

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
  closureBonus: string;
  comments: string;
  numberOfCandidates: number;
  createdAt: string;
};

export type NewJob = {
  client: string;
  name: string;
  status: JobStatus;
  wantedCvs: number;
  contractType: ContractTypeName;
  experienceRangeMin: number;
  experienceRangeMax: number;
  noticePeriodInDays: number;
  skills: RawSkill[];
  salaryBudget: number;
  currency: Currency;
  description: string;
  bonusPayPerCv: number;
  closureBonus: string;
  comments: string;
};

export type JobResponse = { id: number; job: Job };

export type ContractTypeName = 'PERMANENT' | 'TEMPORARY';
export type ContractType = {
  contractTypeName: ContractTypeName;
};
export type JobStatus = 'OPEN' | 'NO_CV_ACCEPTED' | 'CLOSED' | 'ARCHIVED';
export type Currency = 'INR';
