export type UserDto = {
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

export type RecruiterDto = Pick<UserDto, 'id' | 'username'>;
export type ApproverDto = RecruiterDto;

export type RoleName = 'ROLE_RECRUITER' | 'ROLE_ADMIN';
export type Role = {
  id: number;
  name: RoleName;
};

// request to signup
export type UserSignupForm = Omit<
  UserDto,
  'id' | 'roles' | 'approved' | 'approver' | 'approvedOn' | 'createdAt'
> & {
  password: string;
  roleName: RoleName;
};

// request to login
export type UserLoginForm = {
  usernameOrEmail: string;
  password: string;
};
// response from login
export type AuthUser = {
  userId: number;
  username: string;
  token: string;
  tokenType: string;
};
export type AuthUserInfoDto = {
  id: number;
  username: string;
  roleName: RoleName;
};

export type ApprovalRequest = {
  userId: number;
  approved: boolean;
  comments: string;
};
