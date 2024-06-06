import { type Questionnaire } from './schema';
import axiosApi from '../api';
import type { NewQuestionnaire } from '../question/schema';

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
  console.log('create questionnaire', questionnaire)
  const { data } = await api.post(`/questionnaires/`, questionnaire);
  return data;
};

export const updateQuestionnaire = async (
  title: string,
  questionnaire: Questionnaire
): Promise<Questionnaire> => {
  console.log('update questionnaire', questionnaire)
  const { data } = await api.post(`/questionnaires/${title}`, questionnaire);
  return data;
};
