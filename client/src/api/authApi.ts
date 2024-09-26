import baseApi from './baseApi';
import { type SignupRequest, type SignupResponse } from '../types/authTypes';

// vars
const api = baseApi();
const baseApiPath = '/auth';

// functions
export const signup = async (request: SignupRequest): Promise<SignupResponse> => {
  const res = (await api.post(`${baseApiPath}/signup`, request)).data as SignupResponse;
  return res;
};
