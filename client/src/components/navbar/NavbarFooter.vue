<script setup lang="ts">
import Button from 'primevue/button';
import Avatar from 'primevue/avatar';

import { useUserStore } from '../../stores/user/index';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const router = useRouter();

const { tag, role } = defineProps<{
  tag: string;
  role: string;
}>();
function logout() {
  userStore.logout();
  router.push({ name: 'Login' });
}
</script>

<template>
  <div class="flex justify-between">
    <div class="flex justify-between gap-2">
      <Avatar
        icon="pi pi-user"
        class="mr-2"
        size="large"
        style="background-color: #ece9fc; color: #2a1261"
        shape="circle"
      />
      <div class="flex flex-col">
        <h2 class="font-bold">{{ tag }}</h2>
        <h4 class="font-normal">{{ role }}</h4>
      </div>
    </div>
    <Button @click="logout()" size="small" icon="pi pi-sign-out" />
  </div>
</template>
