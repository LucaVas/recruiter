import type { CandidateDto } from "../candidate/types";
import type { JobDto } from "../job/types";
import type { RecruiterDto } from "../user/types";

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
  createdAt: Date;
};

export type UpdateCandidacyRequest = Omit<
  CandidacyDto,
  | 'job'
  | 'recruiter'
  | 'candidate'
  | 'createdAt'
  | 'comments'
>

export type NewCandidacyDto = Omit<
  CandidacyDto,
  'id' | 'job' | 'candidate' | 'recruiter' | 'comments' | 'createdAt'
> & { jobId: number; candidateId: number };

export type CandidacyResponse = {
    id: number,
    candidacy: CandidacyDto
}