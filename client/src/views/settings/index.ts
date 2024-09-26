import type { User } from '@/types/userTypes';
import { ref } from 'vue';

export const user = ref<User>();
export const loading = ref(false);
export const updatingUser = ref(false);
export const openUserProfileModal = ref(false);
