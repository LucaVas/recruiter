<script setup lang="ts">
import StackedLayout from './StackedLayout.vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/user/index';

const router = useRouter();
const userStore = useUserStore();

const links = ref([
  {
    label: 'Jobs',
    items: [
      {
        icon: 'pi pi-list',
        label: 'All jobs',
        component: 'Dashboard',
        shortcut: '⌘+J',
        route: '/',
      },
      {
        icon: 'pi pi-plus',
        label: 'New job',
        component: 'Dashboard',
        shortcut: '⌘+N',
        route: '/jobs/new',
      },
      {
        icon: 'pi pi-plus',
        label: 'New candidate',
        component: 'Dashboard',
        shortcut: '⌘+C',
        route: '/candidates/new',
      },
    ],
  },
  {
    label: 'Users',
    items: [
      {
        icon: 'pi pi-users',
        label: 'All users',
        component: 'Dashboard',
        shortcut: '⌘+U',
        route: '/',
      },
    ],
  },
  {
    label: 'Profile',
    items: [
      {
        label: 'My performance',
        icon: 'pi pi-chart-line',
        shortcut: '⌘+P',
        route: '/',
      },
      {
        label: 'Settings',
        icon: 'pi pi-cog',
        shortcut: '⌘+S',
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

const tag = ref('janny_doe');
const role = ref('Admin');
</script>

<template>
  <StackedLayout :links="links" :role="role" :tag="tag"></StackedLayout>
</template>
