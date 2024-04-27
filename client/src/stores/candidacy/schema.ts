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
export type Candidacy = z.infer<typeof candidacySchema>;

// request
export const updateCandidacyRequestSchema = candidacySchema.omit({
  job: true,
  recruiter: true,
  candidate: true,
  createdDTime: true,
});
export type UpdateCandidacyRequest = z.infer<typeof updateCandidacyRequestSchema>;

export const newCandidacyRequestSchema = updateCandidacyRequestSchema.extend({
  jobId: z.number(),
  candidatePan: z.string(),
});
export type NewCandidacyRequest = z.infer<typeof newCandidacyRequestSchema>;

// response
export type CandidacyResponse = {
  candidacy: Candidacy;
};

// frontend types
export type RawCandidacy = UpdateCandidacyRequest;
