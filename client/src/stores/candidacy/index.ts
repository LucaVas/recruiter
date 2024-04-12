import type { CandidacyResponse, NewCandidacyDto, UpdateCandidacyRequest } from './types';
import axiosApi from '../api';

const api = axiosApi();

export async function submitCandidacy(candidacy: NewCandidacyDto): Promise<void> {
  await api.post(`/candidacies`, candidacy);
}

export async function updateCandidacy(
  candidacy: UpdateCandidacyRequest
): Promise<CandidacyResponse> {
  const { data } = await api.put(`/candidacies/${candidacy.id}`, candidacy);
  return data;
}

export async function getCandidacy(id: number): Promise<CandidacyResponse> {
  const { data } = await api.get(`/candidacies/${id}`);
  return data;
}
