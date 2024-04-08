import type { CandidateResponse, NewCandidacyDto, NewCandidateDto } from './types';
import axiosApi from '../api';

const api = axiosApi();

export async function addCandidate(newCandidate: NewCandidateDto): Promise<CandidateResponse> {
  const { data } = await api.post(`/candidates`, newCandidate);
  return data;
}
export async function findCandidateByPan(pan: string): Promise<CandidateResponse> {
  const { data } = await api.get(`/candidates/pan/${pan}`);
  return data;
}
export async function submitCandidacy(candidacy: NewCandidacyDto): Promise<void> {
  await api.post(`/candidacies`, candidacy);
}
