import axios from 'axios';
import { getStoredAccessToken } from '@/utils/auth';
import type { CandidateResponse, NewCandidacyDto, NewCandidateDto } from './types';
import { ApiError } from '../../utils/types';

const axiosApi = axios.create({
  baseURL: `http://localhost:8080/api/v1`,
  timeout: 1000,
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${getStoredAccessToken(localStorage)}`,
  },
});

export async function addCandidate(newCandidate: NewCandidateDto): Promise<CandidateResponse> {
  try {
    const { data } = await axiosApi.post(`/candidates`, newCandidate);
    return data;
  } catch (err) {
    if (axios.isAxiosError(err)) {
      throw new ApiError(err.response?.data.message);
    } else {
      throw new ApiError('An unexpected error occurred');
    }
  }
}
export async function findCandidateByPan(pan: string): Promise<CandidateResponse> {
  try {
    const { data } = await axiosApi.get(`/candidates/pan/${pan}`);
    return data;
  } catch (err) {
    if (axios.isAxiosError(err)) {
      throw new ApiError(err.response?.data.message);
    } else {
      throw new ApiError('An unexpected error occurred');
    }
  }
}
export async function submitCandidacy(candidacy: NewCandidacyDto): Promise<void> {
  try {
    await axiosApi.post(`/candidacies`, candidacy);
  } catch (err) {
    if (axios.isAxiosError(err)) {
      throw new ApiError(err.response?.data.message);
    } else {
      throw new ApiError('An unexpected error occurred');
    }
  }
}
