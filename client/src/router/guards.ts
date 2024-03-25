import { useUserStore } from '@/stores/user';

export const authenticate = () => {
  const userStore = useUserStore();
  if (!userStore.isLoggedIn) return { name: 'Login' };

  return true;
};

// export const showForAdmin = () => {
//   if (!isAdmin.value) return { name: 'MyHome' };

//   return true;
// };

export const hideForAuth = () => {
  const userStore = useUserStore();
  if (userStore.isLoggedIn) return { name: 'Dashboard' };

  return true;
};
