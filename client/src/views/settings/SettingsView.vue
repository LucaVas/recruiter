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
import { onMounted, ref } from 'vue';
import type { User } from '@/stores/user/schema';
import { getProfileInformation, updateProfileInformation } from '@/stores/auth/index';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '@/utils/types';
import SettingsHeader from '@/components/settings/SettingsHeader.vue';
import SettingsFooter from '@/components/settings/SettingsFooter.vue';
import SettingsBody from '@/components/settings/SettingsBody.vue';
import UserProfileModal from '@/components/settings/UserProfileModal.vue';
import type { UserInfoUpdateRequest } from '@/stores/auth/schema';

const toast = useToast();
const user = ref<User>();
const loading = ref(false);
const updatingUser = ref(false);
const openUserProfileModal = ref(false);

const update = async (userForm: UserInfoUpdateRequest) => {
  updatingUser.value = true;
  try {
    await updateProfileInformation(userForm);
    openUserProfileModal.value = false;
    toast.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Profile information updated successfully!',
      life: 3000,
    });
    await initUserInformation();
  } catch (e) {
    if (e instanceof ApiError)
      toast.add({ severity: 'error', summary: 'Error', detail: e.message });
  } finally {
    updatingUser.value = false;
  }
};

const initUserInformation = async () => {
  loading.value = true;
  try {
    user.value = await getProfileInformation();
  } catch (e) {
    if (e instanceof ApiError)
      toast.add({ severity: 'error', summary: 'Error', detail: e.message });
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  loading.value = true;
  try {
    await initUserInformation();
  } catch (e) {
    if (e instanceof ApiError)
      toast.add({ severity: 'error', summary: 'Error', detail: e.message });
  } finally {
    loading.value = false;
  }
});
</script>
