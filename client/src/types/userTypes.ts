import type { RoleName } from './roleTypes';

export type User = {
  id: number;
  name: string;
  email: string;
  phone: string;
  city: string;
  country: string;
  roleName: RoleName;
  comments: string;
  approved: boolean;
  approver: Recruiter;
  approvedAt: Date;
  createdAt: Date;
  updatedAt: Date;
};

export type Recruiter = Pick<User, 'id' | 'name'>;
export type ApproverDto = Recruiter;

export type UserApprovalRequest = {
  userId: number;
  approved: boolean;
  comment: string;
};

export type PasswordResetRequest = {
  userId: number;
  newPassword: string;
};
export type PasswordResetTokenRequest = { email: string; name: string };

export type NewUserRequest = Pick<User, 'name' | 'email' | 'phone' | 'city' | 'country'> & {
  roleName: RoleName;
};
