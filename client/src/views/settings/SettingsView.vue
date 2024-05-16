<template>
  <div class="h-full w-full">
    <div v-if="!loading && user" class="flex h-full flex-col justify-between">
      <UserProfileModal
        :user="user"
        :visible="openUserProfileModal"
        @save="(userForm) => update(userForm)"
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
import Divider from 'primevue/divider';
import ProgressSpinner from 'primevue/progressspinner';
import { onMounted } from 'vue';
import { getProfileInformation, updateProfileInformation } from '@/stores/user';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '@/utils/types';
import SettingsHeader from '@/components/settings/SettingsHeader.vue';
import SettingsFooter from '@/components/settings/SettingsFooter.vue';
import SettingsBody from '@/components/settings/SettingsBody.vue';
import UserProfileModal from '@/components/settings/UserProfileModal.vue';
import type { UserInfoUpdateRequest } from '@/stores/auth/schema';
import { updatingUser, openUserProfileModal, loading, user } from './index';
import { showError, showSuccess } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';

const toast = useToast();

const update = async (userForm: UserInfoUpdateRequest) => {
  updatingUser.value = true;
  try {
    await updateProfileInformation(userForm);
    openUserProfileModal.value = false;
    showSuccess(toast, 'Profile information updated successfully!');
    await initUserInformation();
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
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
    if (err instanceof ApiError) showError(toast, err.message);
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
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    loading.value = false;
  }
});
</script>
