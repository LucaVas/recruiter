import type { Client } from '../client/schema';

export interface Questionnaire {
  title: string;
  client: Client;
  questions: Question[];
}

export type QuestionnaireDto = Questionnaire & { id: number; createdAt: string; updatedAt: string };
export type NewQuestionnaire = Omit<Questionnaire, 'questions'> & { questions: NewQuestion[] };
export type UpdatedQuestionnaire = Omit<Questionnaire, 'clientName'>;

export interface Question {
  text: string;
  answer: string | null;
  questionType: QuestionType;
}
export type QuestionType = 'SHORT' | 'PARAGRAPH' | 'YES_NO' | 'OPEN_QUESTION';

export type QuestionDto = Question & { id: number; createdAt: string; updatedAt: string };
export type NewQuestion = Question;

export type QuestionForm = Pick<Question, 'text' | 'answer'> & { skillNames: string[] };
