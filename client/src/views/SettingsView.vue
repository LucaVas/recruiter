<template>
  <div class="h-full w-full">
    <div v-if="!loading && user" class="flex h-full flex-col justify-between">
      <UserProfileModal
        :user="user"
        :visible="openUserProfileModal"
        @save="(userForm, loginRequired) => update(userForm, loginRequired)"
        @close="openUserProfileModal = false"
      />
      <SettingsHeader :user="user" />
      <SettingsBody :user="user" />
      <Divider />
      <SettingsFooter :user="user" @edit="openUserProfileModal = true" />
    </div>
    <div v-else class="flex w-full flex-row justify-center">
      <ProgressSpinner />
    </div>
  </div>
</template>

<script setup lang="ts">
import SettingsHeader from '@/components/settings/SettingsHeader.vue';
import SettingsFooter from '@/components/settings/SettingsFooter.vue';
import SettingsBody from '@/components/settings/SettingsBody.vue';
import UserProfileModal from '@/components/modals/UserProfileModal.vue';
import Divider from 'primevue/divider';
import ProgressSpinner from 'primevue/progressspinner';
import { onMounted } from 'vue';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '@/utils/types';
import { showError, showSuccess } from '@/utils/errorUtils';
import { getProfileInformation, updateProfileInformation } from '@/api/userApi';
import type { UserInfoUpdateRequest } from '@/types/authTypes';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import useAuthStore from '@/stores/authStore';
import { ref } from 'vue';
import type { User } from '@/types/userTypes';

const toast = useToast();
const authStore = useAuthStore();

const user = ref<User>();
const loading = ref(false);
const updatingUser = ref(false);
const openUserProfileModal = ref(false);

const update = async (userForm: UserInfoUpdateRequest, loginRequired: boolean) => {
  updatingUser.value = true;
  try {
    await updateProfileInformation(userForm);
    openUserProfileModal.value = false;
    if (loginRequired) {
      authStore.logout();
      showSuccess(
        toast,
        'Profile information updated successfully! You will be redirected to the login page shortly'
      );
      setTimeout(() => {
        window.location.reload();
        return;
      }, 2000);
    } else {
      showSuccess(toast, 'Profile information updated successfully!');
      await initUserInformation();
    }
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    updatingUser.value = false;
  }
};

const initUserInformation = async () => {
  loading.value = true;
  try {
    user.value = await getProfileInformation();
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  loading.value = true;
  try {
    await initUserInformation();
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    loading.value = false;
  }
});
</script>
