import type { Candidate } from '../candidate/schema';
import type { Job } from '../job/schema';
import type { Recruiter } from '../user/schema';

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
  modifiedDTime: Date;
  createdDTime: Date;
};

export type candidacyComment = {
  id: number;
  text: string;
  createdDTime: Date;
  modifiedDTime: Date;
};

// request
export type UpdateCandidacyRequest = Omit<Candidacy, 'id' | 'createdDTime' | 'modifiedDTime'>;
export type NewCandidacyRequest = UpdateCandidacyRequest & { jobId: number; candidatePan: string };

// response
export type CandidacyResponse = {
  candidacy: Candidacy;
};

// frontend types
export type RawCandidacy = UpdateCandidacyRequest;
