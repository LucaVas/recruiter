import type { Question } from '../question/types';

// backend dtos
export type Skill = {
  id: number;
  name: string;
  questions: Question[];
};
