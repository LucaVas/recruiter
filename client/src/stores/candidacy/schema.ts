import type { Candidate } from '../candidate/schema';
import type { Job } from '../job/schema';
import type { Recruiter, User } from '../user/schema';

export type CandidacyStatus = 'SENT_TO_CLIENT' | 'REJECTED' | 'ACCEPTED';

export type Candidacy = {
  job: Job;
  recruiter: Recruiter;
  candidate: Candidate;
  relevantExperience: number;
  expectedCtc: number;
  officialNoticePeriod: number;
  actualNoticePeriod: number;
  reasonForQuickJoin: string;
  recruiterComment: string;
  status?: CandidacyStatus;
  updatedAt: Date;
  createdAt: Date;
};

export type CandidacyComment = {
  id: number;
  text: string;
  author: User;
  createdAt: Date;
  updatedAt: Date;
};

export type CandidacyFile = {
  id: number;
  type: number;
  name: string;
  uniqueId: string;
  createdAt: Date;
  updatedAt: Date;
};

// request
export type UpdateCandidacyRequest = Omit<
  Candidacy,
  'id' | 'createdAt' | 'updatedAt' | 'recruiter' | 'job' | 'candidate'
>;
export type NewCandidacyRequest = UpdateCandidacyRequest & { jobId: number; candidatePan: string };
export type NewCandidacyCommentRequest = Pick<CandidacyComment, 'text'>;
export type UploadCandidacyFilesRequest = {
  files: File[];
};

// frontend types
export type RawCandidacy = Omit<
  Candidacy,
  'job' | 'recruiter' | 'candidate' | 'createdAt' | 'updatedAt'
>;
