import {z} from 'zod';

const candidacySchema = z.object({
  id: z.number(),
  relevantExperience: z.number().min(0),
  expectedCtc: z.number().min(0),
  officialNoticePeriod: z.number().min(0),
  actualNoticePeriod: z.number().min(0),
  reasonForQuickJoin: z.string(),
  remarks: z.string(),
  modifiedDTime: z.date(),
  createdDTime: z.date(),
});
export type Candidacy = z.infer<typeof candidacySchema>;

const candidacyCommentSchema = z.object({
  id: z.number(),
  text: z.string(),
  createdDTime: z.date(),
  modifiedDTime: z.date()
})
export type candidacyComment = z.infer<typeof candidacyCommentSchema>;

// request
export const updateCandidacyRequestSchema = candidacySchema.omit({
  id: true,
  createdDTime: true,
  modifiedDTime: true,
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
