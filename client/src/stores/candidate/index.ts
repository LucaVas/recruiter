import type { CandidateDto, CandidateResponse, RawCandidateDto } from './types';
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

export async function updateCandidate(candidate: RawCandidateDto) {
  const { data } = await api.put(`/candidates`, candidate);
  return data;
}

export async function getAllCandidates(): Promise<CandidateDto[]> {
  const { data } = await api.get('/candidates');
  return data;
}
