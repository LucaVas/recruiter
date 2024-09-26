import { isAdmin, isLoggedIn } from '@/api/authApi';

export const authenticate = () => {
  if (!isLoggedIn.value) return { name: 'Login' };

  return true;
};

export const showForAdmin = () => {
  if (!isAdmin.value) return { name: 'Dashboard' };

  return true;
};

export const hideForAdmin = () => {
  if (isAdmin.value) return { name: 'Dashboard' };

  return true;
};

export const hideForAuth = () => {
  if (isLoggedIn.value) return { name: 'Dashboard' };

  return true;
};
