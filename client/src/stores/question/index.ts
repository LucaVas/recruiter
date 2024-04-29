import { type Question } from './schema';
import axiosApi from '../api';

// vars
const api = axiosApi();

// functions
export async function searchQuestions(clientOrSkill: string): Promise<Question[]> {
  const { data } = await api.get(`/questions/search=q?${clientOrSkill}`);
  return data;
}
