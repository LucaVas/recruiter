import type { CandidateResponse, RawCandidateDto } from './types';
import axiosApi from '../api';

const api = axiosApi();

export async function createCandidate(newCandidate: RawCandidateDto): Promise<CandidateResponse> {
  const { data } = await api.post(`/candidates`, newCandidate);
  return data;
}
export async function findCandidate(identifier: string): Promise<CandidateResponse> {
  const { data } = await api.get(`/candidates/${identifier}`);
  return data;
}

