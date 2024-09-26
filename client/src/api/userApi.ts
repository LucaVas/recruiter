import axiosApi from './baseApi';
import type {
  ChangePasswordRequest,
  NewPasswordRequest,
  PasswordForgotRequest,
  UserInfoUpdateRequest,
} from '../types/authTypes';
import type { AxiosResponse } from 'axios';
import type { CustomPage } from '../types/paginationTypes';
import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from '@/consts';
import { type NewUserRequest, type User, type UserApprovalRequest } from '../types/userTypes';

// vars
const api = axiosApi();
const baseApi = '/users';

// functions
export async function approveUser(approvalRequest: UserApprovalRequest): Promise<void> {
  await api.post(`${baseApi}/approvals`, approvalRequest);
}

export async function getAllUsers(pageNumber: number, pageSize: number): Promise<CustomPage<User>> {
  const { data } = (await api.get(
    `${baseApi}?page=${pageNumber ?? DEFAULT_PAGE_NUMBER}&pageSize=${pageSize ?? DEFAULT_PAGE_SIZE}`
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
