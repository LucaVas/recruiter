import type { SignupRequest } from '@/stores/auth/schema';
import { ref } from 'vue';

export const signupCommentsModalOpen = ref(false);
export const countries = ref([{ label: 'India', value: 'india' }]);
export const roles = ref([
  { label: 'Recruiter', value: 'ROLE_RECRUITER' },
  { label: 'Admin', value: 'ROLE_ADMIN' },
]);
export const userForm = ref<SignupRequest>({
  name: '',
  email: '',
  password: '',
  phone: '',
  city: '',
  country: '',
  roleName: 'ROLE_RECRUITER',
  comments: '',
});

export const hasSucceeded = ref(false);
export const loading = ref(false);
