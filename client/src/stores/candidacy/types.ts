import type { Candidate } from '../candidate/types';
import type { Job } from '../job/types';
import type { Recruiter } from '../user/types';

// backend dtos
export type Candidacy = {
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
  'job' | 'recruiter' | 'candidate' | 'createdAt'
>;
export type NewCandidacyRequest = UpdateCandidacyRequest & {
  jobId: number;
  candidatePan: string;
};
export type CandidacyResponse = {
  candidacy: Candidacy;
};

// frontend types
export type RawCandidacy = UpdateCandidacyRequest;
