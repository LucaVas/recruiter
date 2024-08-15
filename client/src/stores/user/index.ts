import { type NewUserRequest, type User, type UserApprovalRequest } from './schema';
import axiosApi from '../api';
import type {
  ChangePasswordRequest,
  NewPasswordRequest,
  PasswordForgotRequest,
  UserInfoUpdateRequest,
} from '../auth/schema';
import type { AxiosResponse } from 'axios';

// vars
const api = axiosApi();
const baseApi = '/users';

type CustomPage<T> = {
  users: T[];
  page: number
  totalPages: number;
  totalElements: number;
}

// functions
export async function approveUser(approvalRequest: UserApprovalRequest): Promise<void> {
  await api.post(`${baseApi}/approvals`, approvalRequest);
}

export async function getAllUsers(page: number, pageSize: number): Promise<CustomPage<User>> {
  const { data } = (await api.get(
    `${baseApi}?page=${page ?? 0}&pageSize=${pageSize ?? 5}`
  )) as AxiosResponse<CustomPage<User>>;
  return data;
}

export const createNewUser = async (form: NewUserRequest): Promise<void> => {
  await api.post(`${baseApi}`, form);
};

export const getProfileInformation = async (): Promise<User> => {
  const { data } = await api.get(`${baseApi}/profile`);
  return data;
};

export const updateProfileInformation = async (
  profileInformation: UserInfoUpdateRequest
): Promise<void> => {
  const { data } = await api.put(`${baseApi}/profile/update`, profileInformation);
  return data;
};

export const requestNewPassword = async (form: PasswordForgotRequest): Promise<void> => {
  const { data } = await api.post('/resetPassword', form);
  return data;
};

export const resetPassword = async (token: string, form: NewPasswordRequest): Promise<void> => {
  await api.post(`/resetPassword/${token}`, form);
};

export const changePassword = async (form: ChangePasswordRequest): Promise<void> => {
  await api.post(`/users/password`, form);
};
