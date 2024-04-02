<script setup lang="ts">
import type { MenuItem } from '../../layouts/DashboardLayout.vue';
import NavbarGroup from './NavbarGroup.vue';

const { menuItems } = defineProps<{
  menuItems: MenuItem[];
}>();
defineEmits<{
  (e: 'closeMenu'): void;
}>();
</script>

<template>
  <div class="flex flex-col justify-between overflow-y-auto">
    <NavbarGroup @closeMenu="$emit('closeMenu')" v-for="item in menuItems" :menuItem="item" :key="item.group" />
  </div>
</template>
