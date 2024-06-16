import type { NewSkill, Skill } from './schema';
import axiosApi from '../api';

const api = axiosApi();
const baseApi = '/skills';

export const getAllSkills = async (): Promise<Skill[]> => {
  const { data } = await api.get(baseApi);
  return data;
};

export const createSkill = async (skill: NewSkill): Promise<Skill> => {
  const { data } = await api.post(baseApi, skill);
  return data;
};
