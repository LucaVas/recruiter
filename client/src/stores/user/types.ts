export type UserRole = 'USER_ADMIN' | 'USER_RECRUITER';

export type UserSignupForm = {
  name: string;
  username: string;
  email: string;
  password: string;
  mobile: string;
  city: string;
  country: string;
};

export type UserLoginForm = {
  usernameOrEmail: string;
  password: string;
};

export type AuthUser = {
  token: string;
};

export type User = {
  id: number;
};
