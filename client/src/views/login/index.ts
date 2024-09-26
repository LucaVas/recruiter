import type { LoginRequest } from '@/types/authTypes';
import { ref } from 'vue';

export const loading = ref(false);

export const userForm = ref<LoginRequest>({
  email: '',
  password: '',
});
