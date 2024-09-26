import type { NewPasswordRequest } from '@/types/authTypes';
import { ref } from 'vue';

export const resettingPassword = ref(false);
export const passwordReset = ref(false);

export const form = ref<NewPasswordRequest>({
  password: '',
});
