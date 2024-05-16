import { type User, type UserApprovalRequest } from './schema';
import axiosApi from '../api';
import type {
  NewPasswordRequest,
  PasswordForgotRequest,
  UserInfoUpdateRequest,
} from '../auth/schema';

// vars
const api = axiosApi();

// functions
export async function approveUser(approvalRequest: UserApprovalRequest): Promise<void> {
  await api.post(`/users/approvals`, approvalRequest);
}

export async function getAllUsers(): Promise<User[]> {
  const { data } = await api.get('/users');
  return data;
}

export const getProfileInformation = async (): Promise<User> => {
  const { data } = await api.get('/users/profile');
  return data;
};

export const updateProfileInformation = async (
  profileInformation: UserInfoUpdateRequest
): Promise<void> => {
  const { data } = await api.put('/users/profile/update', profileInformation);
  return data;
};

export const requestNewPassword = async (form: PasswordForgotRequest): Promise<void> => {
  const { data } = await api.post('/resetPassword', form);
  return data;
};

export const resetPassword = async (token: string, form: NewPasswordRequest): Promise<void> => {
  await api.post(`/resetPassword/${token}`, form);
};
