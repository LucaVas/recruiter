import { roleNameSchema } from '../user/schema';
import {z} from 'zod';

export const AuthUserInfoDtoSchema = z.object({
  id: z.number(),
  username: z.string(),
  roleName: roleNameSchema,
});
export type AuthUserInfoDto = z.infer<typeof AuthUserInfoDtoSchema>;


// request
export const loginRequestSchema = z.object({
  usernameOrEmail: z.string(),
  password: z.string(),
});
export type LoginRequest = z.infer<typeof loginRequestSchema>;

export const signupRequestSchema = z.object({
  username: z.string(),
  email: z.string(),
  password: z.string(),
  roleName: roleNameSchema,
});
export type SignupRequest = z.infer<typeof signupRequestSchema>;

// response
export type SignupResponse = { id: number };
export type LoginResponse = {
  user: AuthUserInfoDto;
  token: string;
  tokenType: string;
};
