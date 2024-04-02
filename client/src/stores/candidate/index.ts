import axios, { type AxiosResponse } from 'axios';
import { defineStore } from 'pinia';
import { getIdFromToken, getStoredAccessToken, getUsernameFromToken } from '@/utils/auth';
import type { CandidateDto, CandidateResponse, NewCandidateDto } from './types';
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
    const res = await axiosApi.post(`/candidates`, {
      ...newCandidate,
      recruiter: { id: authUserId, username: authUsername },
    });
    console.log(res.data);
    return res.data;
  }
  async function findCandidateByPan(pan: string): Promise<CandidateDto> {
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
  return { addCandidate, findCandidateByPan };
});
