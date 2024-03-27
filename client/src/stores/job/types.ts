import type { Skill } from '../skill/types';

export type Job = {
  id: number;
  client: string;
  name: string;
  status: JobStatus;
  wantedCVs: number;
  skills: Skill[];
  contractType: ContractType;
  experienceRange: number;
  noticePeriodInDays: number;
  salaryBudget: number;
  description: string;
  bonusPayPerCV: number;
  closureBonus: string;
  comments: string;
  numberOfCandidates: number;
  createdAt: Date;
};

export type ContractType = 'PERMANENT' | 'TEMPORARY';
export type JobStatus = 'OPEN' | 'NO_CV_ACCEPTED' | 'CLOSED' | 'ARCHIVED';
