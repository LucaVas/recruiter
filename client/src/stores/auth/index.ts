import axiosApi from '../api';
import {
  clearStoredAccessToken,
  getAuthRoleFromToken,
  getAuthUserIdFromToken,
  getAuthNameFromToken,
  getStoredAccessToken,
  storeAccessToken,
} from '@/utils/auth';
import {
  type LoginRequest,
  type LoginResponse,
  type SignupRequest,
  type SignupResponse,
} from './schema';
import { computed, ref } from 'vue';

// vars
const api = axiosApi();
const baseApi = '/auth';

const authToken = ref(getStoredAccessToken(localStorage));
export const authUserId = computed(() =>
  authToken.value ? getAuthUserIdFromToken(authToken.value) : null
);
export const authName = computed(() =>
  authToken.value ? getAuthNameFromToken(authToken.value) : null
);
export const isLoggedIn = computed(() => !!authToken.value);
export const invalidToken = computed(() => authToken.value);
export const isAdmin = computed(() =>
  authToken.value ? getAuthRoleFromToken(authToken.value) === 'ADMIN' : false
);

// functions
export const signup = async(request: SignupRequest): Promise<SignupResponse> => {
  const res = (await api.post(`${baseApi}/signup`, request)).data as SignupResponse;
  return res;
}
export const login = async(request: LoginRequest): Promise<void> => {
  const res = (await api.post(`${baseApi}/login`, request)).data as LoginResponse;
  authToken.value = res.token;
  storeAccessToken(localStorage, res.token);
}
export const logout = () => {
  clearStoredAccessToken(localStorage);
}
