<script setup lang="ts">
import StackedLayout from './StackedLayout.vue';
import { onBeforeMount, ref, onMounted } from 'vue';
import { username, role, getMe, isLoggedIn } from '../stores/user/index';
import { getMenuItems, getRoleTag } from './utils';
import type { RoleName } from '@/stores/user/types';

export type MenuItem = {
  group: string;
  links: {
    icon: string;
    name: string;
    shortcut: string;
    view: string;
    privileges: RoleName[];
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
        privileges: ['ROLE_RECRUITER', 'ROLE_ADMIN'],
      },
      {
        icon: 'pi pi-plus',
        name: 'New job',
        shortcut: '⌘+N',
        view: 'NewJob',
        privileges: ['ROLE_ADMIN'],
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
        privileges: ['ROLE_ADMIN'],
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
        privileges: ['ROLE_RECRUITER', 'ROLE_ADMIN'],
      },
      {
        name: 'Settings',
        icon: 'pi pi-cog',
        shortcut: '⌘+S',
        view: 'Dashboard',
        privileges: ['ROLE_RECRUITER', 'ROLE_ADMIN'],
      },
    ],
  },
]);

const roleTag = ref('');
const roleMenuItems = ref<MenuItem[]>();
onMounted(async () => {
  if (!role.value) {
    roleTag.value = getRoleTag(role.value);
    roleMenuItems.value = getMenuItems(role.value, menuItems.value);
  }
});
</script>

<template>
  <StackedLayout
    :menuItems="roleMenuItems ?? menuItems"
    :username="username"
    :role="roleTag"
  ></StackedLayout>
</template>
