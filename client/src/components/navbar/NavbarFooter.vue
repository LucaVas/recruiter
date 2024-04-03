<script setup lang="ts">
import Button from 'primevue/button';
import Avatar from 'primevue/avatar';
import { useRouter } from 'vue-router';
import { logout } from '../../stores/user/index';

const router = useRouter();

const { username, role } = defineProps<{
  username: string;
  role: string;
}>();

function logOut() {
  logout();
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
        <h2 class="font-bold">{{ username }}</h2>
        <h4 class="font-normal">{{ role }}</h4>
      </div>
    </div>
    <Button @click="logOut()" size="small" icon="pi pi-sign-out" />
  </div>
</template>
