import axios, { type AxiosResponse } from 'axios';
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
import { ApiError } from '@/utils/types';

const authorizedHeaders = {
  'Content-Type': 'application/json',
  Authorization: `Bearer ${getStoredAccessToken(localStorage)}`,
};

const unauthorizedHeaders = {
  'Content-Type': 'application/json',
};

const axiosApi = axios.create({
  baseURL: `http://localhost:8080/api/v1`,
  timeout: 1000,
});

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
  try {
    console.log(form);
    const { data } = await axiosApi.post(`/auth/signup`, form, { headers: unauthorizedHeaders });
    return data;
  } catch (err) {
    if (axios.isAxiosError(err)) {
      throw new ApiError(err.response?.data.message);
    } else {
      throw new ApiError('An unexpected error occurred');
    }
  }
}

export async function login(form: UserLoginForm): Promise<void> {
  try {
    const res: AxiosResponse<AuthUser> = await axiosApi.post(`/auth/login`, form, {
      headers: authorizedHeaders,
    });
    userId.value = res.data.userId;
    username.value = res.data.username;
    authToken.value = res.data.token;
    storeAccessToken(localStorage, authToken.value);
  } catch (err) {
    if (axios.isAxiosError(err)) {
      throw new ApiError(err.response?.data.message);
    } else {
      throw new ApiError('An unexpected error occurred');
    }
  }
}

export function logout() {
  authToken.value = null;
  clearStoredAccessToken(localStorage);
}

export async function approveUser(approvalRequest: ApprovalRequest): Promise<void> {
  try {
    await axiosApi.post(`/users/approvals`, approvalRequest, { headers: authorizedHeaders });
  } catch (err) {
    if (axios.isAxiosError(err)) {
      throw new ApiError(err.response?.data.message);
    } else {
      throw new ApiError('An unexpected error occurred');
    }
  }
}

export async function getAllUsers(): Promise<UserDto[]> {
  try {
    const { data } = await axiosApi.get('/users', { headers: authorizedHeaders });
    return data;
  } catch (err) {
    if (axios.isAxiosError(err)) {
      throw new ApiError(err.response?.data.message);
    } else {
      throw new ApiError('An unexpected error occurred');
    }
  }
}

export async function getMe(): Promise<void> {
  try {
    const { data } = (await axiosApi.get('/auth/me', {
      headers: authorizedHeaders,
    })) as AxiosResponse<AuthUserInfoDto>;
    userId.value = data.id;
    username.value = data.username;
    role.value = data.roleName;
  } catch (err) {
    if (axios.isAxiosError(err)) {
      throw new ApiError(err.response?.data.message);
    } else {
      throw new ApiError('An unexpected error occurred');
    }
  }
}
