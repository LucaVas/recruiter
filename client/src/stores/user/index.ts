import axios, { type AxiosResponse } from 'axios';
import type { UserSignupForm, UserLoginForm, AuthUser } from './types';
import { computed, ref } from 'vue';
import { clearStoredAccessToken, getStoredAccessToken, storeAccessToken } from '@/utils/auth';
import { defineStore } from 'pinia';

const axiosApi = axios.create({
  baseURL: `http://localhost:8080/api/v1`,
  timeout: 1000,
  headers: { 'Content-Type': 'application/json' },
});

export const useUserStore = defineStore('userStore', () => {
  const userId = ref()
  const username = ref('')
  const authToken = ref(getStoredAccessToken(localStorage));
  const isLoggedIn = computed(() => !!authToken.value);
  const invalidToken = computed(() => !!authToken.value);

  function signup(form: UserSignupForm): Promise<AxiosResponse<{ id: number }>> {
    return axiosApi.post(`/auth/signup`, form);
  }

  async function login(form: UserLoginForm): Promise<void> {
    const res: AxiosResponse<AuthUser> = await axiosApi.post(`/auth/login`, form);
    userId.value = res.data.userId;
    username.value = res.data.username;
    authToken.value = res.data.token;
    storeAccessToken(localStorage, authToken.value);
  }

  function logout() {
    authToken.value = null;
    clearStoredAccessToken(localStorage);
  }

  return { userId, username, authToken, isLoggedIn, invalidToken, signup, login, logout };
});

// export const isAdmin = computed(() =>
//   authToken.value ? getUserRoleFromToken(authToken.value) === 'admin' : false
// );

// export async function login(userLogin: { email: string; password: string }) {
//   const { token } = await trpc.user.login.query(userLogin);

//   authToken.value = token;
//   storeAccessToken(localStorage, token);
// }
