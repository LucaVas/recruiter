import { type User, type UserApprovalRequest } from './schema';
import axiosApi from '../api';
import type {
  NewPasswordRequest,
  PasswordForgotRequest,
  UserInfoUpdateRequest,
} from '../auth/schema';

// vars
const api = axiosApi();
const baseApi = '/users'

// functions
export async function approveUser(approvalRequest: UserApprovalRequest): Promise<void> {
  await api.post(`${baseApi}/approvals`, approvalRequest);
}

export async function getAllUsers(): Promise<User[]> {
  const { data } = await api.get(baseApi);
  return data;
}

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
