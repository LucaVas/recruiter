import { type Questionnaire } from './schema';
import axiosApi from '../api';

// vars
const api = axiosApi();

// functions
export async function getQuestionnairesByClient(clientOrTitle: string): Promise<Questionnaire[]> {
  const { data } = await api.get(`/questionnaires/search?clientOrTitle=${clientOrTitle}`);
  return data;
}
