import { type Candidate, type CandidateResponse, type NewCandidateRequest } from './schema';
import axiosApi from '../api';

const api = axiosApi();
const baseApi = '/candidates';

export async function createCandidate(
  newCandidate: NewCandidateRequest
): Promise<CandidateResponse> {
  const { data } = await api.post(baseApi, newCandidate);
  return data;
}

export async function findCandidate(identifier: string): Promise<CandidateResponse> {
  const { data } = await api.get(`${baseApi}/${identifier}`);
  return data;
}

export async function updateCandidate(candidate: NewCandidateRequest) {
  const { data } = await api.put(baseApi, candidate);
  return data;
}

export async function getAllCandidates(): Promise<Candidate[]> {
  const { data } = await api.get(baseApi);
  return data;
}
