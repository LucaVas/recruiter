// backend dtos
export type User = {
  id: number;
  username: string;
  email: string;
  mobile: string;
  city: string;
  country: string;
  roles: Set<Role>;
  comments: string;
  approved: boolean;
  approver: ApproverDto;
  approvedOn: Date;
  createdAt: Date;
};
export type Recruiter = Pick<User, 'id' | 'username'>;
export type ApproverDto = Recruiter;

// backend domain objects
export type RoleName = 'ROLE_RECRUITER' | 'ROLE_ADMIN';
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

// frontend types
export type Role = {
  id: number;
  name: RoleName;
};
