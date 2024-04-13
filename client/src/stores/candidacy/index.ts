import type {
  Candidacy,
  CandidacyResponse,
  NewCandidacyRequest,
  UpdateCandidacyRequest,
} from './types';
import axiosApi from '../api';

const api = axiosApi();

export async function submitCandidacy(candidacy: NewCandidacyRequest): Promise<void> {
  await api.post(`/candidacies`, candidacy);
}

export async function updateCandidacy(
  id: number,
  candidacy: UpdateCandidacyRequest
): Promise<CandidacyResponse> {
  const { data } = await api.put(`/candidacies/${id}`, candidacy);
  return data;
}

export async function getCandidacy(id: number): Promise<CandidacyResponse> {
  const { data } = await api.get(`/candidacies/${id}`);
  return data;
}

export async function getAllCandidacies(): Promise<Candidacy[]> {
  const { data } = await api.get(`/candidacies`);
  return data;
}
