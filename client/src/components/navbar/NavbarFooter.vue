<script setup lang="ts">
import Button from 'primevue/button';
import Avatar from 'primevue/avatar';
import { useRouter } from 'vue-router';
import { logout } from '@/api/authApi';

const router = useRouter();

const { name } = defineProps<{
  name: string;
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
      <h2 class="font-bold">{{ name }}</h2>
    </div>
    <Button @click="logOut()" size="small" icon="pi pi-sign-out" />
  </div>
</template>
