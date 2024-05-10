import type { LoginRequest } from '@/stores/auth/schema';
import { ref } from 'vue';

export const loading = ref(false);

export const userForm = ref<LoginRequest>({
  usernameOrEmail: '',
  password: '',
});
