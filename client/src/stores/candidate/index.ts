import axios from 'axios';
import { defineStore } from 'pinia';
import { getStoredAccessToken } from '@/utils/auth';
import type { Candidate, CandidateResponse } from './types';

const axiosApi = axios.create({
  baseURL: `http://localhost:8080/api/v1`,
  timeout: 1000,
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${getStoredAccessToken(localStorage)}`,
  },
});

export const useCandidateStore = defineStore('candidateStore', () => {
  async function addCandidate(newCandidate: Candidate): Promise<CandidateResponse> {
    const res = await axiosApi.post(`/candidates`, newCandidate);
    console.log(res.data);
    return res.data;
  }
  return { addCandidate };
});
