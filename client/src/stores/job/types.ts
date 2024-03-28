import type { Skill } from '../skill/types';

export type Job = {
  id: number;
  client: string;
  name: string;
  status: JobStatus;
  wantedCVs: number;
  skills: Skill[];
  contractType: ContractType;
  experienceRange: string;
  noticePeriodInDays: number;
  salaryBudget: number;
  description: string;
  bonusPayPerCV: number;
  closureBonus: string;
  comments: string;
  numberOfCandidates: number;
  createdAt: string;
};

export type ContractType = {
  contractTypeName: 'PERMANENT' | 'TEMPORARY';
};
export type JobStatus = 'OPEN' | 'NO_CV_ACCEPTED' | 'CLOSED' | 'ARCHIVED';
