import { z } from 'zod';
import { questionSchema } from '../question/schema';

export const skillSchema = z.object({
  id: z.number(),
  name: z.string(),
  questions: z.array(questionSchema),
});

export type Skill = z.infer<typeof skillSchema>;
