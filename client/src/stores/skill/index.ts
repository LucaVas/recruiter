import axios from 'axios';
import type { RawSkillDto } from './types';
import { getStoredAccessToken } from '@/utils/auth';
import { ApiError } from '@/utils/types';

const axiosApi = axios.create({
  baseURL: `http://localhost:8080/api/v1`,
  timeout: 1000,
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${getStoredAccessToken(localStorage)}`,
  },
});

export async function getAllSkills(): Promise<RawSkillDto[]> {
  try {
    const { data } = await axiosApi.get(`/skills`);
    return data;
  } catch (err) {
    if (axios.isAxiosError(err)) {
      throw new ApiError(err.response?.data.message);
    } else {
      throw new ApiError('An unexpected error occurred');
    }
  }
}
