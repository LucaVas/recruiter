import axios, { type AxiosResponse } from 'axios';
import type { UserSignupForm, UserLoginForm, AuthUser, UserDto, ApprovalRequest } from './types';
import { computed, ref } from 'vue';
import { clearStoredAccessToken, getStoredAccessToken, storeAccessToken } from '@/utils/auth';
import { defineStore } from 'pinia';
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

export const useUserStore = defineStore('userStore', () => {
  const userId = ref();
  const username = ref('');
  const authToken = ref(getStoredAccessToken(localStorage));
  const isLoggedIn = computed(() => !!authToken.value);
  const invalidToken = computed(() => !!authToken.value);

  async function getAllUsers(): Promise<UserDto[]> {
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

  async function signup(form: UserSignupForm): Promise<AxiosResponse<{ id: number }>> {
    try {
      console.log(form)
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

  async function login(form: UserLoginForm): Promise<void> {
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

  function logout() {
    authToken.value = null;
    clearStoredAccessToken(localStorage);
  }

  async function approveUser(approvalRequest: ApprovalRequest): Promise<void> {
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

  return {
    userId,
    username,
    authToken,
    isLoggedIn,
    invalidToken,
    signup,
    login,
    logout,
    getAllUsers,
    approveUser,
  };
});

// export const isAdmin = computed(() =>
//   authToken.value ? getUserRoleFromToken(authToken.value) === 'admin' : false
// );

// export async function login(userLogin: { email: string; password: string }) {
//   const { token } = await trpc.user.login.query(userLogin);

//   authToken.value = token;
//   storeAccessToken(localStorage, token);
// }
