import { type Question, type QuestionForm } from './schema';
import axiosApi from '../api';

// vars
const api = axiosApi();

// functions
export async function searchQuestions(clientOrSkill: string): Promise<Question[]> {
  const { data } = await api.get(`/questions/search?clientOrSkill=${clientOrSkill}`);
  return data;
}

export const createQuestion = async (question: QuestionForm): Promise<Question> => {
  const { data } = await api.post('/questions', question);
  return data;
};
