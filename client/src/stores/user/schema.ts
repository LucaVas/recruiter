// backend dtos
export type User = {
  id: number;
  name: string;
  email: string;
  phone: string;
  city: string;
  country: string;
  role: Role;
  comments: string;
  approved: boolean;
  approver: Recruiter;
  approvedAt: Date;
  createdAt: Date;
  updatedAt: Date;
};

export type Recruiter = Pick<User, 'id' | 'name'>;
export type ApproverDto = Recruiter;

// backend domain objects
export type Role = {
  id: number;
  name: RoleName;
  description: string;
  createdAt: Date;
  updatedAt: Date;
};
export type RoleName = 'RECRUITER' | 'ADMIN' | 'TESTER';

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
