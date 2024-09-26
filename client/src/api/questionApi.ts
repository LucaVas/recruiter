import { type Question, type QuestionForm } from '../types/questionTypes';
import axiosApi from './baseApi';

// vars
const api = axiosApi();
const baseApi = '/questions';

// functions
export async function searchQuestions(titleOrClientOrSkill: string): Promise<Question[]> {
  const { data } = await api.get(`${baseApi}/search?titleOrClientOrSkill=${titleOrClientOrSkill}`);
  return data;
}

export const createQuestion = async (question: QuestionForm): Promise<Question> => {
  const { data } = await api.post(`${baseApi}`, question);
  return data;
};
