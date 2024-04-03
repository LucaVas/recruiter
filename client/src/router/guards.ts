import { isLoggedIn, isAdmin } from '@/stores/user';

export const authenticate = () => {
  if (!isLoggedIn) return { name: 'Login' };

  return true;
};

export const showForAdmin = () => {
  if (!isAdmin) return { name: 'Dashboard' };

  return true;
};

export const hideForAuth = () => {
  if (isAdmin) return { name: 'Dashboard' };

  return true;
};
