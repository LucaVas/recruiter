import { type Candidate, type CandidateResponse, type NewCandidate } from './schema';
import axiosApi from '../api';

const api = axiosApi();
const baseApi = '/candidates';

export async function createCandidate(newCandidate: NewCandidate): Promise<CandidateResponse> {
  const { data } = await api.post(`${baseApi}`, newCandidate);
  return data;
}

export async function findCandidate(identifier: string): Promise<CandidateResponse> {
  const { data } = await api.get(`${baseApi}/${identifier}`);
  return data;
}

export async function updateCandidate(candidate: NewCandidate) {
  const { data } = await api.put(`${baseApi}`, candidate);
  return data;
}

export async function getAllCandidates(): Promise<Candidate[]> {
  const { data } = await api.get(`${baseApi}`);
  return data;
}
