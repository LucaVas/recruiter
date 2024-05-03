<template>
  <Toast />
  <div class="h-full w-full">
    <div v-if="!loading && user" class="flex h-full flex-col justify-between">
      <SettingsHeader :user="user" />
      <SettingsBody :user="user" />
      <Divider />
      <SettingsFooter :user="user" />
    </div>
    <div v-else class="flex w-full flex-row justify-center">
      <ProgressSpinner />
    </div>
  </div>
</template>

<script setup lang="ts">
import Toast from 'primevue/toast';
import Divider from 'primevue/divider';
import ProgressSpinner from 'primevue/progressspinner';
import { onMounted, ref } from 'vue';
import type { User } from '@/stores/user/schema';
import { getProfileInformation } from '@/stores/auth/index';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '../../utils/types';
import SettingsHeader from './SettingsHeader.vue';
import SettingsFooter from './SettingsFooter.vue';
import SettingsBody from './SettingsBody.vue';

const toast = useToast();
const user = ref<User>();
const loading = ref(false);
onMounted(async () => {
  loading.value = true;
  try {
    user.value = await getProfileInformation();
  } catch (e) {
    if (e instanceof ApiError) toast.add({ severity: 'error', summary: 'Error', detail: e });
  } finally {
    loading.value = false;
  }
});
</script>
