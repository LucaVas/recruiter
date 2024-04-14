import { Chance } from 'chance';

export const random = process.env.CI ? Chance(1) : Chance();
const usernamePool = 'abcdefghijklmnopqrstuvwxyz1234567890_';
const phonePool = '1234567890';

export const fakeRecruiter = () => ({
  email: random.email(),
  username: random.string({ pool: usernamePool }),
  password: 'Password123!',
  mobile: random.phone({ pool: phonePool, length: 10 }),
  city: random.city(),
  country: 'India',
  roleName: 'RECRUITER'
});
