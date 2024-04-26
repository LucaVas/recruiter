import { z } from 'zod';

export const roleNameSchema = z.enum(['ROLE_RECRUITER', 'ROLE_ADMIN']);
export const roleSchema = z.object({
  id: z.number(),
  name: roleNameSchema,
});
export const recruiterSchema = z.object({
  id: z.number(),
  username: z.string(),
});

export const userSchema = z.object({
  id: z.number(),
  username: z.string(),
  email: z.string().email(),
  mobile: z.string(),
  city: z.string(),
  country: z.string(),
  roles: z.array(roleSchema),
  comments: z.string(),
  approved: z.boolean(),
  approver: recruiterSchema,
  approvedDTime: z.date(),
  createdDTime: z.date(),
});

// backend dtos
export type User = z.infer<typeof userSchema>;
export type Recruiter = z.infer<typeof recruiterSchema>;
export type ApproverDto = Recruiter;

// backend domain objects
export type Role = z.infer<typeof roleSchema>;
export type RoleName = z.infer<typeof roleNameSchema>;
export type UserApprovalRequest = {
  userId: number;
  approved: boolean;
  comments: string;
};
export type PasswordResetRequest = {
  newPassword: string;
};
export type PasswordResetTokenRequest = {
  email: string;
  username: string;
};

