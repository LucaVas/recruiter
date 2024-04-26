import type { Skill } from './schema';
import axiosApi from '../api';

const api = axiosApi();

export async function getAllSkills(): Promise<Skill[]> {
  const { data } = await api.get(`/skills`);
  return data;
}
