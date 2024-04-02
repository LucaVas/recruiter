import type { JobDto } from "../job/types";
import type { RecruiterDto } from "../user/types";

export type CandidateDto = {
  id: number;
  name: string;
  phone: string;
  email: string;
  totalExperience: number;
  education: string;
  currentCtc: number;
  pan: string;
  createdAt: Date;
};

export type CandidacyDto = {
  id: number;
  job: JobDto;
  recruiter: RecruiterDto;
  candidate: CandidateDto;
  relevantExperience: number;
  expectedCtc: number;
  officialNoticePeriod: number;
  actualNoticePeriod: number;
  reasonForQuickJoin: string;
  remarks: string;
  comments: string;
  cvRatePaymentDate: Date;
  closureBonusPaymentDate: Date;
  createdAt: Date;
};

export type NewCandidateDto = Omit<CandidateDto, 'id' | 'createdAt'>;

export type Recruiter = {
  id: number;
  username: string;
};
export type CandidateResponse = { id: number; candidate: CandidateDto };
