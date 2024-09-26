<template>
  <StackedLayout
    :menuItems="getMenuItems(authStore.isAdmin, menuItems) ?? menuItems"
    :name="authStore.authUserName ?? 'null'"
  />
</template>

<script setup lang="ts">
import StackedLayout from '../StackedLayout.vue';
import { getMenuItems } from '../utils';
import { menuItems } from './index';
import useAuthStore from '@/stores/authStore';

const authStore = useAuthStore();
</script>
