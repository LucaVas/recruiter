import type { LoginRequest } from '@/stores/auth/schema';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export const router = useRouter();
export const loading = ref(false);

export const userForm = ref<LoginRequest>({
  usernameOrEmail: '',
  password: '',
});
