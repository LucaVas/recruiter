<template>
  <StackedLayout
    :menuItems="getMenuItems(isAdmin, menuItems) ?? menuItems"
    :name="authName ?? 'null'"
  />
</template>

<script setup lang="ts">
import StackedLayout from '../StackedLayout.vue';
import { getMenuItems } from '../utils';
import { menuItems } from './index';
import { authName, isAdmin } from '../../stores/auth/index';
</script>
