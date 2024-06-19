import { type Questionnaire, type UpdatedQuestionnaire } from './schema';
import axiosApi from '../api';
import type { NewQuestionnaire, QuestionnaireDto } from '../questionnaire/schema';

// vars
const api = axiosApi();
const baseApi = '/questionnaires';

// functions
export const getQuestionnairesById = async (
  clientName: string,
  title: string
): Promise<Questionnaire> => {
  const { data } = await api.get(`${baseApi}/${clientName}/${title}`);
  return data;
};

export const getQuestionnairesByClient = async (
  clientOrTitle: string
): Promise<Questionnaire[]> => {
  const { data } = await api.get(`${baseApi}/search?clientOrTitle=${clientOrTitle}`);
  return data;
};

export const getAllQuestionnaires = async (): Promise<Questionnaire[]> => {
  const { data } = await api.get(`${baseApi}`);
  return data;
};

export const saveNewQuestionnaire = async (
  questionnaire: NewQuestionnaire
): Promise<Questionnaire> => {
  const { data } = await api.post(`${baseApi}`, questionnaire);
  return data;
};

export const updateQuestionnaire = async (
  id: number,
  questionnaire: UpdatedQuestionnaire
): Promise<Questionnaire> => {
  const { data } = await api.put(`${baseApi}/${id}`, questionnaire);
  return data;
};
