import axios from 'axios';
import { defineStore } from 'pinia';
import { getIdFromToken, getStoredAccessToken, getUsernameFromToken } from '@/utils/auth';
import type { CandidateResponse, NewCandidateDto } from './types';
import { computed, ref } from 'vue';

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
  return { addCandidate };
});
