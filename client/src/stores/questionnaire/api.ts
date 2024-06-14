import { type Questionnaire, type UpdatedQuestionnaire } from './schema';
import axiosApi from '../api';
import type { NewQuestionnaire } from '../questionnaire/schema';

// vars
const api = axiosApi();

// functions
export const getQuestionnairesById = async (
  clientName: string, title: string
): Promise<Questionnaire> => {
  const { data } = await api.get(`/questionnaires/${clientName}/${title}`);
  return data;
};

export const getQuestionnairesByClient = async (
  clientOrTitle: string
): Promise<Questionnaire[]> => {
  const { data } = await api.get(`/questionnaires/search?clientOrTitle=${clientOrTitle}`);
  return data;
};

export const getAllQuestionnaires = async (): Promise<Questionnaire[]> => {
  const { data } = await api.get(`/questionnaires/`);
  return data;
};

export const saveNewQuestionnaire = async (
  questionnaire: NewQuestionnaire
): Promise<Questionnaire> => {
  const { data } = await api.post(`/questionnaires/`, questionnaire);
  return data;
};

export const updateQuestionnaire = async (
  title: string,
  questionnaire: UpdatedQuestionnaire
): Promise<Questionnaire> => {
  const { data } = await api.post(`/questionnaires/${title}`, questionnaire);
  return data;
};
