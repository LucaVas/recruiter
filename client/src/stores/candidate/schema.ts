import { z } from "zod";

export const candidateStatusSchema = z.enum(['ACTIVE', 'ARCHIVED']);
export const candidateSchema = z.object({
  name: z.string(),
  phone: z.string(),
  email: z.string(),
  totalExperience: z.number(),
  education: z.string(),
  currentCtc: z.number(),
  pan: z.string(),
  createdDTime: z.date(),
});

// backend dtos
export type Candidate = z.infer<typeof candidateSchema>;

//request
export const newCandidateRequestSchema = candidateSchema.omit({
  createdDTime: true,
});
export type NewCandidateRequest = z.infer<typeof newCandidateRequestSchema>;

export const updateCandidateRequestSchema = newCandidateRequestSchema;
export type UpdateCandidateRequest = z.infer<typeof updateCandidateRequestSchema>;

// response
export type CandidateResponse = { pan: string; candidate: Candidate };


// backend enums
export type CandidateStatus = z.infer<typeof candidateStatusSchema>;
