// backend dtos
export type User = {
  id: number;
  username: string;
  email: string;
  mobile: string;
  city: string;
  country: string;
  roles: Role[];
  comments: string;
  approved: boolean;
  approver: Recruiter;
  approvedDTime: Date;
  createdDTime: Date;
  modifiedDTime: Date;
};

export type Recruiter = Pick<User, 'id' | 'username'>;
export type ApproverDto = Recruiter;

// backend domain objects
export type Role = { id: number; name: RoleName };
export type RoleName = 'ROLE_RECRUITER' | 'ROLE_ADMIN';

export type UserApprovalRequest = {
  userId: number;
  approved: boolean;
  comment: string;
};

export type PasswordResetRequest = {
  userId: number;
  newPassword: string;
};
export type PasswordResetTokenRequest = { email: string; username: string };
