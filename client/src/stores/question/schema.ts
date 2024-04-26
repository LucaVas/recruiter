import { z } from "zod";

export const questionSchema = z.object({
  id: z.number(),
  text: z.string(),
});

// backend dtos
export type Question = z.infer<typeof questionSchema>;
