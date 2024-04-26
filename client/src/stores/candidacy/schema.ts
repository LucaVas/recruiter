import { candidateSchema } from '../candidate/schema';
import { jobSchema } from '../job/schema';
import { recruiterSchema } from '../user/schema';
import {z} from 'zod';

const candidacySchema = z.object({
  job: jobSchema,
  recruiter: recruiterSchema,
  candidate: candidateSchema,
  relevantExperience: z.number(),
  expectedCtc: z.number(),
  officialNoticePeriod: z.number(),
  actualNoticePeriod: z.number(),
  reasonForQuickJoin: z.string(),
  remarks: z.string(),
  comments: z.string(),
  createdDTime: z.date(),
});
// backend dtos
export type Candidacy = z.infer<typeof candidacySchema>;

// backend domain objects
export type UpdateCandidacyRequest = Omit<
  Candidacy,
  'job' | 'recruiter' | 'candidate' | 'createdDTime'
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
