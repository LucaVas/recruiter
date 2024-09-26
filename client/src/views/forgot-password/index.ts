import type { PasswordForgotRequest } from '@/types/authTypes';
import { ref } from 'vue';

export const sendingEmail = ref(false);

export const form = ref<PasswordForgotRequest>({
  email: '',
  name: '',
});
