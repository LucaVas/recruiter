<script setup lang="ts">
import StackedLayout from '../StackedLayout.vue';
import { onMounted } from 'vue';
import { username, role, getMe } from '@/stores/user/index';
import { getMenuItems, getRoleTag } from '../utils';
import type { RoleName } from '@/stores/user/types';
import { menuItems, roleMenuItems, roleTag, usernameTag } from './index';

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
