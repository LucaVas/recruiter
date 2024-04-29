import {
  type Candidacy,
  type CandidacyResponse,
  type NewCandidacyRequest,
  type UpdateCandidacyRequest,
} from './schema';
import axiosApi from '../api';

const api = axiosApi();

export async function submitCandidacy(candidacy: NewCandidacyRequest): Promise<void> {
  await api.post(`/candidacies`, candidacy);
}

export async function updateCandidacy(
  jobId: number,
  pan: string,
  candidacy: UpdateCandidacyRequest
): Promise<CandidacyResponse> {
  const { data } = await api.put(`/candidacies/job=${jobId}&candidate=${pan}`, candidacy);
  return data;
}

export async function getCandidacy(jobId: number, pan: string): Promise<CandidacyResponse> {
  const { data } = await api.get(`/candidacies/job=${jobId}&candidate=${pan}`);
  return data;
}

export async function getAllCandidacies(): Promise<Candidacy[]> {
  const { data } = await api.get(`/candidacies`);
  return data;
}
