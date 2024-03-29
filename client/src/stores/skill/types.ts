import type { Question } from "../question/types";

export type Skill = {
  id: number;
  name: string;
  questions: Question[]
};

export type RawSkill = Pick<Skill, 'id' | 'name'>
