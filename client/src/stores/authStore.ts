import type { LoginRequest, LoginResponse } from '@/types/authTypes';
import { defineStore } from 'pinia';
import baseApi from '@/api/baseApi';
import type { RoleName } from '@/types/roleTypes';
import type { AxiosResponse } from 'axios';

const TOKEN_KEY = 'token';
const api = baseApi();
const baseApiPath = '/auth';

type AuthStoreState = {
  authToken: string | null;
  authUserId: number | null;
  authUserName: string | null;
  authUserRole: RoleName | null;
  isAdmin: boolean;
};
const useAuthStore = defineStore('authStore', {
  state: (): AuthStoreState => ({
    authToken: null,
    authUserId: null,
    authUserName: null,
    authUserRole: null,
    isAdmin: false,
  }),
  getters: {
    isLoggedIn: ({ authToken }): boolean => !!authToken,
  },
  actions: {
    async login(request: LoginRequest): Promise<void> {
      const res: AxiosResponse<LoginResponse> = await api.post(`${baseApiPath}/login`, request);
      const data = res.data;
      this.authToken = data.token;
      this.authUserId = data.user.id;
      this.authUserName = data.user.name;
      this.authUserRole = data.user.roleName;
      this.isAdmin = data.user.roleName === 'ADMIN';
      localStorage.setItem(TOKEN_KEY, data.token);
    },
    logout(): void {
      localStorage.removeItem(TOKEN_KEY);
      this.$reset();
    },
  },
  persist: {
    storage: localStorage,
  },
});

export default useAuthStore;
