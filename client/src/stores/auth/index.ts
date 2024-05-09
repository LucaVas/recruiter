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
  type NewPasswordRequest,
  type PasswordForgotRequest,
  type SignupRequest,
  type SignupResponse,
  type UserInfoUpdateRequest,
} from './schema';
import { computed, ref } from 'vue';
import type { User } from '../user/schema';

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

export const getProfileInformation = async (): Promise<User> => {
  const { data } = await api.get('/auth/profile');
  return data;
};

export const updateProfileInformation = async (
  profileInformation: UserInfoUpdateRequest
): Promise<void> => {
  const { data } = await api.put('/auth/profile/update', profileInformation);
  return data;
};

export const requestNewPassword = async (form: PasswordForgotRequest): Promise<void> => {
  const { data } = await api.post('/resetPassword', form);
  return data;
};

export const resetPassword = async (token: string, form: NewPasswordRequest): Promise<void> => {
  await api.post(`/resetPassword/${token}`, form);
};
