import { clientSchema } from '../client/schema';
import { z } from 'zod';
import { skillSchema } from '../skill/schema';

const contractTypeSchema = z.enum(['PERMANENT', 'TEMPORARY']);
const currencySchema = z.enum(['INR']);
const jobStatusSchema = z.enum(['OPEN', 'NO_CV_ACCEPTED', 'CLOSED', 'ARCHIVED']);

export const jobSchema = z.object({
  id: z.number(),
  client: clientSchema,
  name: z.string(),
  status: jobStatusSchema,
  wantedCvs: z.number(),
  skills: z.array(skillSchema),
  contractType: contractTypeSchema,
  experienceRangeMin: z.number(),
  experienceRangeMax: z.number(),
  noticePeriodInDays: z.number(),
  salaryBudget: z.number(),
  currency: currencySchema,
  description: z.string(),
  bonusPayPerCv: z.number(),
  closureBonus: z.number(),
  comments: z.string(),
  numberOfCandidates: z.number().nullable(),
  closureBonusPaymentDate: z.date(),
  cvRatePaymentDate: z.date(),
  createdDTime: z.date()
});


// backend dtos
export type Job = z.infer<typeof jobSchema>;
export type ContractType = z.infer<typeof contractTypeSchema>;
export type Currency = z.infer<typeof currencySchema>;
export type JobStatus = z.infer<typeof jobStatusSchema>;

// backend domain objects
export type JobResponse = { id: number; job: Job };
export type NewJobRequest = Omit<Job, 'id' | 'comments' | 'numberOfCandidates' | 'createdAt'>;
export type UpdateJobRequest = NewJobRequest & { id: number };
export type ChangeJobStatusRequest = { jobStatus: JobStatus };
