import type { RawSkill } from './types';
import axiosApi from '../api';

const api = axiosApi();

export async function getAllSkills(): Promise<RawSkill[]> {
  const { data } = await api.get(`/skills`);
  return data;
}
