<script setup lang="ts">
import StackedLayout from './StackedLayout.vue';
import { ref, onMounted } from 'vue';
import { username, role, getMe } from '../stores/user/index';
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
const usernameTag = ref('');
const roleMenuItems = ref<MenuItem[]>();

function setTags(role: RoleName, username: string) {
  roleTag.value = getRoleTag(role);
  usernameTag.value = username;
  roleMenuItems.value = getMenuItems(role, menuItems.value);
}

onMounted(async () => {
  if (role.value && username.value) {
    setTags(role.value, username.value);
  } else {
    try {
      await getMe();
      setTags(role.value!, username.value!);
    } catch (err) {
      return;
    }
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
