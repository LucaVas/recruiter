<script lang="ts" setup>
import Navbar from '@/components/navbar/Navbar.vue';
import type { MenuItem } from './types';
import { ref } from 'vue';

const { menuItems, username } = defineProps<{
  menuItems: MenuItem[];
  username: string;
}>();
const menuVisible = ref(false);
</script>

<template>
  <div class="flex h-screen flex-col md:flex-row">
    <div class="h-20 w-full p-2 md:relative md:h-full md:w-20 bg-slate-50/90 fixed">
      <nav class="flex items-center w-full h-full justify-end md:justify-center">
        <Button icon="pi pi-bars" @click="menuVisible = true" />
      </nav>
    </div>
    <Navbar
      @closeMenu="menuVisible = false"
      :visible="menuVisible"
      :menuItems="menuItems"
      :username="username"
    />

    <main class="flex flex-1 items-start justify-center overflow-auto p-5 pt-24 md:pt-5">
      <RouterView />
    </main>
  </div>
</template>
