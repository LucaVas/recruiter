export interface Question {
  text: string;
  answer: string | null;
  questionType: QuestionType;
}
export type QuestionType = 'SHORT' | 'PARAGRAPH' | 'YES_NO' | 'OPEN_QUESTION';

export type QuestionDto = Question & { id: number };
export type NewQuestion = Question;


export type QuestionForm = Pick<Question, 'text' | 'answer'> & { skillNames: string[] };
