<script setup lang="ts">
import StackedLayout from './StackedLayout.vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/user/index';

const router = useRouter();
const userStore = useUserStore();

const links = ref([
  {
    label: 'Documents',
    items: [
      { icon: 'pi pi-list', label: 'My jobs', component: 'Dashboard', shortcut: '⌘+J', route: '/' },
      {
        icon: 'pi pi-search',
        label: 'Search',
        component: 'Dashboard',
        shortcut: '⌘+S',
        route: '/',
      },
      { icon: 'pi pi-plus', label: 'New job', component: 'Dashboard', shortcut: '⌘+N', route: '/' },
    ],
  },
  {
    label: 'Profile',
    items: [
      {
        label: 'Settings',
        icon: 'pi pi-cog',
        shortcut: '⌘+O',
        route: '/',
      },
      {
        label: 'Messages',
        icon: 'pi pi-inbox',
        badge: 2,
        route: '/',
      },
      {
        label: 'Logout',
        icon: 'pi pi-sign-out',
        shortcut: '⌘+Q',
        route: '',
        command: () => {
          userStore.logout();
          router.push({ name: 'Login' });
        },
      },
    ],
  },
]);

const tag = ref('username');
const role = ref('Recruiter');
</script>

<template>
  <StackedLayout :links="links" :role="role" :tag="tag"></StackedLayout>
</template>
