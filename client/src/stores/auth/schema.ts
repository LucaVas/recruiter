import type { RoleName, User } from '../user/schema';
import {z} from 'zod';

export const AuthUserInfoDtoSchema = z.object({
  id: z.number(),
  username: z.string(),
  roleName: z.enum(['ROLE_USER', 'ROLE_ADMIN']),
});
export type AuthUserInfoDto = z.infer<typeof AuthUserInfoDtoSchema>;


// backend domain objects
export type LoginRequest = {
  usernameOrEmail: string;
  password: string;
};
export type LoginResponse = {
  user: AuthUserInfoDto;
  token: string;
  tokenType: string;
};
export type SignupRequest = Omit<
  User,
  'id' | 'roles' | 'approved' | 'approver' | 'approvedOn' | 'createdAt'
> & {
  password: string;
  roleName: RoleName;
};
export type SignupResponse = { id: number };
