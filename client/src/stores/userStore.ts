import { defineStore } from 'pinia';
import baseApi from '@/api/baseApi';
import type { AxiosResponse } from 'axios';
import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from '@/consts';
import type { CustomPage } from '@/types/paginationTypes';
import type { NewUserRequest, User, UserApprovalRequest } from '@/types/userTypes';

const api = baseApi();
const baseApiPath = '/users';

type UserStoreState = {
  users: User[];
  totalUsers: number;
};
const useUserStore = defineStore('userStore', {
  state: (): UserStoreState => ({
    users: [],
    totalUsers: 0,
  }),
  getters: {},
  actions: {
    async fetchUsers(
      pageNumber = DEFAULT_PAGE_NUMBER,
      pageSize = DEFAULT_PAGE_SIZE
    ): Promise<void> {
      const { data }: AxiosResponse<CustomPage<User>> = await api.get(
        `${baseApiPath}?page=${pageNumber}&pageSize=${pageSize}`
      );
      this.users = data.elements;
      this.totalUsers = data.totalElements;
    },
    async approveUser(approvalRequest: UserApprovalRequest): Promise<void> {
      await api.post(`${baseApiPath}/approvals`, approvalRequest);
      await this.fetchUsers();
    },
    async createNewUser(form: NewUserRequest): Promise<void> {
      await api.post(`${baseApiPath}`, form);
      await this.fetchUsers();
    },
  },
});

export default useUserStore;
