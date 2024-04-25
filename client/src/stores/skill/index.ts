import type { Skill } from './types';
import axiosApi from '../api';

const api = axiosApi();

export async function getAllSkills(): Promise<Skill[]> {
  const { data } = await api.get(`/skills`);
  return data;
}
