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
      { icon: 'pi pi-plus', label: 'New job', component: 'Dashboard', shortcut: '⌘+N', route: '/' },
    ],
  },
  {
    label: 'Candidates',
    items: [
      {
        icon: 'pi pi-plus',
        label: 'New candidate',
        component: 'Dashboard',
        shortcut: '⌘+N',
        route: '/',
      },
    ],
  },
  {
    label: 'Users',
    items: [
      {
        icon: 'pi pi-list',
        label: 'All users',
        component: 'Dashboard',
        shortcut: '⌘+J',
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
        shortcut: '⌘+O',
        route: '/',
      },
      {
        label: 'Settings',
        icon: 'pi pi-cog',
        shortcut: '⌘+O',
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
