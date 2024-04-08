import { type AxiosResponse } from 'axios';
import type {
  UserSignupForm,
  UserLoginForm,
  AuthUser,
  UserDto,
  ApprovalRequest,
  AuthUserInfoDto,
  RoleName,
} from './types';
import { computed, ref } from 'vue';
import {
  clearStoredAccessToken,
  getIsAdminFromToken,
  getStoredAccessToken,
  storeAccessToken,
} from '@/utils/auth';
import axiosApi from '../api';


// consts
const api = axiosApi();
const authToken = ref(getStoredAccessToken(localStorage));

export const userId = ref();
export const username = ref('');
export const isLoggedIn = computed(() => !!authToken.value);
export const invalidToken = computed(() => !!authToken.value);
export const role = ref<RoleName>();
export const isAdmin = computed(() =>
  authToken.value ? getIsAdminFromToken(authToken.value) === true : false
);

// functions
export async function signup(form: UserSignupForm): Promise<AxiosResponse<{ id: number }>> {
  const { data } = await api.post(`/auth/signup`, form);
  return data;
}

export async function login(form: UserLoginForm): Promise<void> {
  const res: AxiosResponse<AuthUser> = await api.post(`/auth/login`, form);
  userId.value = res.data.userId;
  username.value = res.data.username;
  authToken.value = res.data.token;
  storeAccessToken(localStorage, authToken.value);
  await getMe();
}

export function logout() {
  authToken.value = null;
  clearStoredAccessToken(localStorage);
}

export async function approveUser(approvalRequest: ApprovalRequest): Promise<void> {
  await api.post(`/users/approvals`, approvalRequest);
}

export async function getAllUsers(): Promise<UserDto[]> {
  const { data } = await api.get('/users');
  return data;
}

export async function getMe(): Promise<void> {
  const { data } = (await api.get('/auth/me')) as AxiosResponse<AuthUserInfoDto>;
  userId.value = data.id;
  username.value = data.username;
  role.value = data.roleName;
}
