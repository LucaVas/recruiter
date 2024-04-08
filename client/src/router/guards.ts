import { isLoggedIn, role } from '@/stores/user';

export const authenticate = () => {
  if (!isLoggedIn.value) return { name: 'Login' };

  return true;
};

export const showForAdmin = () => {
  if (role.value !== 'ROLE_ADMIN') return { name: 'Dashboard' };

  return true;
};

export const hideForAuth = () => {
  if (role.value === 'ROLE_ADMIN') return { name: 'Dashboard' };

  return true;
};
