<script lang="ts" setup>
import Navbar from '@/components/navbar/Navbar.vue';
import type { MenuItem } from './types';
import { ref } from 'vue';
import NavbarButtons from '@/components/navbar/NavbarButtons.vue';

const { menuItems, name } = defineProps<{
  menuItems: MenuItem[];
  name: string;
}>();
const menuVisible = ref(false);
</script>

<template>
  <div class="flex h-screen flex-col md:flex-row">
    <div class="fixed h-20 w-full bg-slate-50/90 p-2 md:relative md:h-full md:w-20">
      <nav
        class="flex h-full w-full items-start justify-end md:flex-col md:items-center md:justify-start md:gap-3"
      >
        <Button icon="pi pi-bars" @click="menuVisible = true" />
        <NavbarButtons />
      </nav>
    </div>
    <Navbar
      @closeMenu="menuVisible = false"
      :visible="menuVisible"
      :menuItems="menuItems"
      :name="name"
    />

    <main class="flex flex-1 items-start justify-center overflow-auto p-5 pt-24 md:pt-5">
      <RouterView />
    </main>
  </div>
</template>
