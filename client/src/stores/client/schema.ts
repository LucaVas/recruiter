import { z } from 'zod';

const industrySchema = z.enum([
  'IT',
  'FINANCE',
  'HEALTHCARE',
  'EDUCATION',
  'MARKETING',
  'SALES',
  'HUMAN_RESOURCES',
  'ENGINEERING',
  'OTHER',
]);

export const clientSchema = z.object({
  id: z.number(),
  name: z.string(),
  industry: industrySchema,
  createdDTime: z.date(),
});

export type Industry = z.infer<typeof industrySchema>;
export type Client = z.infer<typeof clientSchema>;
