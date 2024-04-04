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
  <div class="flex h-full flex-col justify-start overflow-y-auto">
    <NavbarGroup
      @closeMenu="$emit('closeMenu')"
      v-for="item in menuItems"
      :menuItem="item"
      :key="item.group"
    />
  </div>
</template>
