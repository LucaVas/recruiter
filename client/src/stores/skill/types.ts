import type { Question } from "../question/types";

export type Skill = {
  id: number;
  name: string;
  questions: Question[]
};
