export type UserRole = 'ADMIN' | 'RECRUITER';

export type UserSignupForm = {
  username: string;
  email: string;
  password: string;
  mobile: string;
  city: string;
  country: string;
  role: UserRole;
};

export type UserLoginForm = {
  email: string;
  password: string;
};

export type UserLoginResponse = {
  token: string;
  username: string;
};

export type User = {
  id: number;
};
