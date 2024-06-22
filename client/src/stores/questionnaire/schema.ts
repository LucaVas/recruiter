import type { Client } from '../client/schema';
import type { NewQuestion, Question } from '../question/schema';

export interface Questionnaire {
  title: string;
  client: Client;
  questions: Question[];
}

export type QuestionnaireDto = Questionnaire & { id: number; createdAt: string; updatedAt: string };
export type NewQuestionnaire = Omit<Questionnaire, 'questions'> & { questions: NewQuestion[] };
export type UpdatedQuestionnaire = Omit<Questionnaire, 'clientName'>;
