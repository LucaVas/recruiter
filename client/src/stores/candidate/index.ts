import { newCandidateRequestSchema, type Candidate, type CandidateResponse, type NewCandidateRequest, updateCandidateRequestSchema } from './schema';
import axiosApi from '../api';

const api = axiosApi();

export async function createCandidate(
  newCandidate: NewCandidateRequest
): Promise<CandidateResponse> {
  newCandidateRequestSchema.parse(newCandidate);
  const { data } = await api.post(`/candidates`, newCandidate);
  return data;
}

export async function findCandidate(identifier: string): Promise<CandidateResponse> {
  const { data } = await api.get(`/candidates/${identifier}`);
  return data;
}

export async function updateCandidate(candidate: Candidate) {
  updateCandidateRequestSchema.parse(candidate);
  const { data } = await api.put(`/candidates`, candidate);
  return data;
}

export async function getAllCandidates(): Promise<Candidate[]> {
  const { data } = await api.get('/candidates');
  return data;
}
