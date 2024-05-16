import type { PasswordForgotRequest } from '@/stores/auth/schema';
import { ref } from 'vue';

export const sendingEmail = ref(false);

export const form = ref<PasswordForgotRequest>({
  email: '',
  name: '',
});
