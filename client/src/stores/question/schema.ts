import { z } from "zod";

export const questionSchema = z.object({
  id: z.number(),
  text: z.string().min(1),
  active: z.boolean(),
  createdDTime: z.date(),
  modifiedDTime: z.date(),
});
export type Question = z.infer<typeof questionSchema>;
