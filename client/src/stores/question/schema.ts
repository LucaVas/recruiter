export type Question = {
  id: number;
  text: string;
  answer: string;
  active: boolean;
  division: string;
  createdDTime: Date;
  modifiedDTime: Date;
};

export type QuestionForm = Pick<Question, 'text' | 'division' | 'answer'> & {
  clientId: number;
  skillId: number;
};
