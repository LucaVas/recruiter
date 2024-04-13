import type { Candidate } from '../candidate/types';
import type { Job } from '../job/types';
import type { Recruiter } from '../user/types';

// backend dtos
export type Candidacy = {
  id: number;
  job: Job;
  recruiter: Recruiter;
  candidate: Candidate;
  relevantExperience: number;
  expectedCtc: number;
  officialNoticePeriod: number;
  actualNoticePeriod: number;
  reasonForQuickJoin: string;
  remarks: string;
  comments: string;
  createdAt: Date;
};

// backend domain objects
export type UpdateCandidacyRequest = Omit<
  Candidacy,
  'id' | 'job' | 'recruiter' | 'candidate' | 'createdAt'
>;
export type NewCandidacyRequest = Omit<UpdateCandidacyRequest, 'id'> & {
  jobId: number;
  candidatePan: string;
};
export type CandidacyResponse = {
  id: number;
  candidacy: Candidacy;
};

// frontend types
export type RawCandidacy = UpdateCandidacyRequest
