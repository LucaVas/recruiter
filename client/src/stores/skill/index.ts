import type { NewSkill, Skill } from './schema';
import axiosApi from '../api';

const api = axiosApi();

export const getAllSkills = async (): Promise<Skill[]> => {
  const { data } = await api.get(`/skills`);
  return data;
};

export const createSkill = async (skill: NewSkill): Promise<Skill> => {
  const { data } = await api.post(`/skills`, skill);
  return data;
};
