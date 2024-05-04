export type AuthUserInfoDto = {
  id: number;
  username: string;
  roleName: string;
};

// request
export type LoginRequest = { usernameOrEmail: string; password: string };
export type SignupRequest = {
  username: string;
  email: string;
  password: string;
  mobile: string;
  city: string;
  country: string;
  roleName: string;
  comments: string;
};
export type UserInfoUpdateRequest = Pick<SignupRequest, 'email' | 'mobile' | 'city'>;

// response
export type SignupResponse = { id: number };
export type LoginResponse = {
  user: AuthUserInfoDto;
  token: string;
  tokenType: string;
};
