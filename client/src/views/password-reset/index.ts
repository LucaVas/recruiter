import type { NewPasswordRequest } from '@/stores/auth/schema';
import { ref } from 'vue';

export const resettingPassword = ref(false);
export const passwordReset = ref(false);

export const form = ref<NewPasswordRequest>({
  password: '',
});
