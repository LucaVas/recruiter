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

export const industries = [
  { name: 'IT', value: 'IT' },
  { name: 'Finance', value: 'FINANCE' },
  { name: 'Healthcare', value: 'HEALTHCARE' },
  { name: 'Education', value: 'EDUCATION' },
  { name: 'Marketing', value: 'MARKETING' },
  { name: 'Sales', value: 'SALES' },
  { name: 'Human Resources', value: 'HUMAN_RESOURCES' },
  { name: 'Engineering', value: 'ENGINEERING' },
  { name: 'Other', value: 'OTHER' },
];

export const clientSchema = z.object({
  name: z.string(),
  industry: industrySchema,
});

export type Industry = z.infer<typeof industrySchema>;
export type Client = z.infer<typeof clientSchema>;