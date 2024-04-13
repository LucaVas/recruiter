import type { RoleName, User } from '../user/types';

// backend domain objects
export type AuthUserInfoDto = {
  id: number;
  username: string;
  roleName: RoleName;
};
export type LoginRequest = {
  usernameOrEmail: string;
  password: string;
};
export type LoginResponse = {
  user: AuthUserInfoDto;
  token: string;
  tokenType: string;
};
export type SignupRequest = Omit<User, 'id' | 'roles' | 'approved' | 'approver' | 'approvedOn' | 'createdAt'> & {
  password: string;
  roleName: RoleName;
};
export type SignupResponse = { id: number };
