<script setup lang="ts">
import type { MenuItem } from '@/layouts/types';
import NavbarBody from './NavbarBody.vue';
import NavbarHeader from './NavbarHeader.vue';
import NavbarFooter from './NavbarFooter.vue';
import Divider from 'primevue/divider';

const { menuItems, visible, name } = defineProps<{
  visible: boolean;
  menuItems: MenuItem[];
  name: string;
}>();
defineEmits<{
  (e: 'closeMenu'): void;
}>();
</script>

<template>
  <div class="flex justify-center">
    <Sidebar :visible="visible">
      <template #container>
        <div class="flex h-full flex-col justify-between px-4 py-6">
          <NavbarHeader @closeMenu="$emit('closeMenu')" />
          <Divider />
          <NavbarBody @closeMenu="$emit('closeMenu')" :menuItems="menuItems" />
          <Divider />
          <NavbarFooter :name="name" />
        </div>
      </template>
    </Sidebar>
  </div>
</template>
