import type { Question } from "../question/schema";

export type Skill = {
  id: number;
  name: string;
  questions: Question[];
};

