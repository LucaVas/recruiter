import type { RoleName } from '../user/schema';

export type AuthUserInfoDto = {
  id: number;
  name: string;
  roleName: RoleName;
};

// request
export type LoginRequest = { email: string; password: string };
export type SignupRequest = {
  name: string;
  email: string;
  password: string;
  phone: string;
  city: string;
  country: string;
  roleName: string;
  comments: string;
};
export type UserInfoUpdateRequest = Pick<SignupRequest, 'email' | 'phone' | 'city'>;
export type PasswordForgotRequest = Pick<SignupRequest, 'email' | 'name'>;
export type NewPasswordRequest = Pick<SignupRequest, 'password'>;
export type ChangePasswordRequest = {
  oldPassword: string;
  newPassword: string;
};

// response
export type SignupResponse = { id: number };
export type LoginResponse = {
  user: AuthUserInfoDto;
  token: string;
  tokenType: string;
};
