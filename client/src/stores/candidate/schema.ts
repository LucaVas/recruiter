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
  createdAt: z.date(),
});

// backend dtos
export type Candidate = z.infer<typeof candidateSchema>;

// backend domain objects
export type CandidateResponse = { pan: string; candidate: Candidate };
export type NewCandidateRequest = Omit<Candidate, 'createdAt'>;
export type UpdateCandidateRequest = NewCandidateRequest;

// backend enums
export type CandidateStatus = z.infer<typeof candidateStatusSchema>;
