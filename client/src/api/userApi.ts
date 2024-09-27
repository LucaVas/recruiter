import axiosApi from './baseApi';
import type {
  ChangePasswordRequest,
  NewPasswordRequest,
  PasswordForgotRequest,
  UserInfoUpdateRequest,
} from '@/types/authTypes';
import type { User } from '@/types/userTypes';

const api = axiosApi();
const baseApi = '/users';

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
