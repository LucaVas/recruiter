import useAuthStore from '@/stores/authStore';
import type { NavigationGuard } from 'vue-router';

export const showForAdminGuard: NavigationGuard = async (to, from, next) => {
  const authStore = useAuthStore();
  if (authStore.isAdmin) {
    next();
  } else {
    return next({ name: 'Dashboard' });
  }
};

export const hideForAdminGuard: NavigationGuard = async (to, from, next) => {
  const authStore = useAuthStore();
  if (!authStore.isAdmin) {
    next();
  } else {
    return next({ name: 'Dashboard' });
  }
};

export const showForAuthGuard: NavigationGuard = async (to, from, next) => {
  const authStore = useAuthStore();
  if (authStore.isLoggedIn) {
    next();
  } else {
    return next({ name: 'Login' });
  }
};

export const hideForAuthGuard: NavigationGuard = async (to, from, next) => {
  const authStore = useAuthStore();
  if (!authStore.isLoggedIn) {
    next();
  } else {
    return next({ name: 'Dashboard' });
  }
};
