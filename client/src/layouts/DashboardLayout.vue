<script setup lang="ts">
import StackedLayout from './StackedLayout.vue';
import { ref } from 'vue';
import { username, isAdmin } from '../stores/user/index';

export type MenuItem = {
  group: string;
  links: {
    icon: string;
    name: string;
    shortcut: string;
    view: string;
    command?: (...args: any) => any;
  }[];
};
const menuItems = ref<MenuItem[]>([
  {
    group: 'Jobs',
    links: [
      {
        icon: 'pi pi-list',
        name: 'All jobs',
        shortcut: '⌘+J',
        view: 'Dashboard',
      },
      {
        icon: 'pi pi-plus',
        name: 'New job',
        shortcut: '⌘+N',
        view: 'NewJob',
      },
    ],
  },
  {
    group: 'Users',
    links: [
      {
        icon: 'pi pi-users',
        name: 'All users',
        shortcut: '⌘+U',
        view: 'UsersView',
      },
    ],
  },
  {
    group: 'Profile',
    links: [
      {
        name: 'My performance',
        icon: 'pi pi-chart-line',
        shortcut: '⌘+P',
        view: 'Dashboard',
      },
      {
        name: 'Settings',
        icon: 'pi pi-cog',
        shortcut: '⌘+S',
        view: 'Dashboard',
      },
    ],
  },
]);

const role = isAdmin ? 'Admin' : 'Recruiter';
</script>

<template>
  <StackedLayout :menuItems="menuItems" :username="username" :role="role"></StackedLayout>
</template>
