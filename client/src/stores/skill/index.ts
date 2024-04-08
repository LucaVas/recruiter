import type { RawSkillDto } from './types';
import axiosApi from '../api';

const api = axiosApi();

export async function getAllSkills(): Promise<RawSkillDto[]> {
    const { data } = await api.get(`/skills`);
    return data;
}
