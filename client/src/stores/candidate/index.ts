import axios, { type AxiosResponse } from 'axios';
import { defineStore } from 'pinia';
import { getIdFromToken, getStoredAccessToken, getUsernameFromToken } from '@/utils/auth';
import type { CandidateDto, CandidateResponse, NewCandidacyDto, NewCandidateDto } from './types';
import { computed, ref } from 'vue';
import { ApiError } from '../../utils/types';

const axiosApi = axios.create({
  baseURL: `http://localhost:8080/api/v1`,
  timeout: 1000,
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${getStoredAccessToken(localStorage)}`,
  },
});

export const useCandidateStore = defineStore('candidateStore', () => {
  const authToken = ref(getStoredAccessToken(localStorage));
  const authUserId = computed(() => (authToken.value ? getIdFromToken(authToken.value) : null));
  const authUsername = computed(() =>
    authToken.value ? getUsernameFromToken(authToken.value) : null
  );

  async function addCandidate(newCandidate: NewCandidateDto): Promise<CandidateResponse> {
    console.log('Add Candidate payload:', newCandidate);
    try {
      const { data } = await axiosApi.post(`/candidates`, newCandidate);
      return data;
    } catch (err) {
      console.log(err);
      if (axios.isAxiosError(err)) {
        throw new ApiError(err.response?.data.message);
      } else {
        throw new ApiError('An unexpected error occurred');
      }
    }
  }
  async function findCandidateByPan(pan: string): Promise<CandidateResponse> {
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
  async function submitCandidacy(candidacy: NewCandidacyDto): Promise<void> {
    try {
      const { data } = await axiosApi.post(`/candidacies`, candidacy);
    } catch (err) {
      console.log(err);
      if (axios.isAxiosError(err)) {
        throw new ApiError(err.response?.data.message);
      } else {
        throw new ApiError('An unexpected error occurred');
      }
    }
  }
  return { addCandidate, findCandidateByPan, submitCandidacy };
});
