<script setup lang="ts">
import StackedLayout from './StackedLayout.vue';
import { ref } from 'vue';
import { useUserStore } from '../stores/user/index';

const userStore = useUserStore();
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
        view: 'Dashboard',
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

const tag = userStore.username !== '' ? userStore.username : 'john_doe';
const role = ref('Admin');
</script>

<template>
  <StackedLayout :menuItems="menuItems" :role="role" :tag="tag"></StackedLayout>
</template>
