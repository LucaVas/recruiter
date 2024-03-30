import type { QuestionDto } from '../question/types';

export type SkillDto = {
  id: number;
  name: string;
  questions: Set<QuestionDto>;
};

export type RawSkillDto = Pick<SkillDto, 'id' | 'name'>;
