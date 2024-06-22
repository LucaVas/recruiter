import type { Candidate } from '../candidate/schema';
import type { Job } from '../job/schema';
import type { User } from '../user/schema';

export type CandidacyStatus = 'SENT_TO_CLIENT' | 'REJECTED' | 'ACCEPTED' | 'WITHDRAWN' | 'ARCHIVED';

export interface Candidacy {
  candidate: Candidate;
  job: Job;
  relevantExperience: number;
  expectedCtc: number;
  officialNoticePeriod: number;
  actualNoticePeriod: number;
  reasonForQuickJoin: string;
  recruiterComment: string;
}

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
export type UpdateCandidacy = Candidacy & { id: number };
export type NewCandidacy = Omit<Candidacy, 'candidate'> & { candidate: Candidate | null };
export type CandidacyDto = Candidacy & {
  id: number;
  recruiter: User;
  status?: CandidacyStatus;
  updatedAt: Date;
  createdAt: Date;
};

export type NewCandidacyCommentRequest = Pick<CandidacyComment, 'text'>;
export type UploadCandidacyFilesRequest = {
  files: File[];
};

// frontend types
export type RawCandidacy = Omit<
  Candidacy,
  'job' | 'recruiter' | 'candidate' | 'createdAt' | 'updatedAt'
>;
