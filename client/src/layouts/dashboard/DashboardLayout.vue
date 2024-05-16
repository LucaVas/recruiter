<template>
  <StackedLayout
    :menuItems="getMenuItems(isAdmin, menuItems) ?? menuItems"
    :username="authUsername ?? 'null'"
  />
</template>

<script setup lang="ts">
import StackedLayout from '../StackedLayout.vue';
import { getMenuItems } from '../utils';
import { menuItems } from './index';
import { authUsername, isAdmin } from '../../stores/auth/index';
</script>
