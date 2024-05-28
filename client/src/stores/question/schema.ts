export type Questionnaire = {
  id: number;
  title: string;
  questions: Question[];
  createdDTime: Date;
  modifiedDTime: Date;
};

export type Question = {
  id: number;
  text: string;
  answer: string;
  active: boolean;
  questionType: QuestionType;
  createdDTime: Date;
  modifiedDTime: Date;
};

export type QuestionType = 'SHORT' | 'PARAGRAPH' | 'YES_NO' | 'OPEN_QUESTION';


export type QuestionForm = Pick<Question, 'text' | 'answer'> & { skillNames: string[] };

export type NewQuestionnaire = Pick<Questionnaire, 'title'> & { questions: NewQuestion[] };

export type NewQuestion = Pick<Question, 'text' | 'answer' | 'questionType'>;
