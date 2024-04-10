import type { SkillDto } from '../skill/types';
import type { RecruiterDto } from '../user/types';

export type JobDto = {
  id: number;
  client: string;
  name: string;
  status: JobStatus;
  wantedCvs: number;
  skills: SkillDto[];
  contractType: ContractTypeDto;
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
  closureBonusPaymentDate: Date;
  cvRatePaymentDate: Date;
  recruiter: RecruiterDto;
  createdAt: string;
};

export type NewJob = Omit<
  JobDto,
  'id' | 'skills' | 'comments' | 'numberOfCandidates' | 'createdAt' | 'recruiter' | 'contractType'
> & { contractType: ContractTypeName };

export type JobResponse = { id: number; job: JobDto };

export type JobStatus = 'OPEN' | 'NO_CV_ACCEPTED' | 'CLOSED' | 'ARCHIVED';

export type ContractTypeDto = {
  contractTypeName: ContractTypeName;
};
export type ContractTypeName = 'PERMANENT' | 'TEMPORARY';

export type Currency = 'INR';
