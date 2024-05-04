export type Question = {
  id: number;
  title: string;
  text: string;
  answer: string;
  active: boolean;
  division: string;
  createdDTime: Date;
  modifiedDTime: Date;
};

export type QuestionForm = Pick<Question, 'title' | 'text' | 'division' | 'answer'> & {
  clientId: number;
  skillNames: string[];
};
