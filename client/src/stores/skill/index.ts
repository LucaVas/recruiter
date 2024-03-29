import axios from 'axios';
import { defineStore } from 'pinia';
import type { RawSkill } from './types';
import { getStoredAccessToken } from '@/utils/auth';

const axiosApi = axios.create({
  baseURL: `http://localhost:8080/api/v1`,
  timeout: 1000,
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${getStoredAccessToken(localStorage)}`,
  },
});

export const useSkillsStore = defineStore('skillsStore', () => {
  async function getAllSkills(): Promise<RawSkill[]> {
    const res = await axiosApi.get(`/skills`);
    return res.data;
  }
  return { getAllSkills };
});
