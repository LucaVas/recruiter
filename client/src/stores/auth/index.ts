import axiosApi from '../api';
import {
  clearStoredAccessToken,
  getAuthRoleFromToken,
  getAuthUserIdFromToken,
  getAuthUsernameFromToken,
  getStoredAccessToken,
  storeAccessToken,
} from '@/utils/auth';
import {
  type AuthUserInfoDto,
  type LoginRequest,
  type LoginResponse,
  type SignupRequest,
  type SignupResponse,
} from './schema';
import { computed, ref } from 'vue';

// vars
const api = axiosApi();
const authToken = ref(getStoredAccessToken(localStorage));
export const authUserId = computed(() =>
  authToken.value ? getAuthUserIdFromToken(authToken.value) : null
);
export const authUsername = computed(() =>
  authToken.value ? getAuthUsernameFromToken(authToken.value) : null
);
export const isLoggedIn = computed(() => !!authToken.value);
export const invalidToken = computed(() => authToken.value);
export const isAdmin = computed(() =>
  authToken.value ? getAuthRoleFromToken(authToken.value) === 'ROLE_ADMIN' : false
);

// functions
export async function signup(request: SignupRequest): Promise<SignupResponse> {
  const res = (await api.post(`/auth/signup`, request)).data as SignupResponse;
  return res;
}
export async function login(request: LoginRequest): Promise<void> {
  const res = (await api.post(`/auth/login`, request)).data as LoginResponse;
  authToken.value = res.token;
  storeAccessToken(localStorage, res.token);
}
export function logout() {
  clearStoredAccessToken(localStorage);
}
export async function getMe(): Promise<AuthUserInfoDto> {
  const res = (await api.get('/auth/me')).data as AuthUserInfoDto;
  return res;
}
