import type { User } from '@/stores/user/schema';
import { ref } from 'vue';

export const user = ref<User>();
export const loading = ref(false);
export const updatingUser = ref(false);
export const openUserProfileModal = ref(false);
