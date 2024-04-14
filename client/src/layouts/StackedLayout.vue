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
  <div class="flex h-screen">
    <nav class="w-20 border border-r-slate-400 bg-slate-100 p-2 text-center">
      <Button icon="pi pi-bars" @click="menuVisible = true" />
      <Navbar
        @closeMenu="menuVisible = false"
        :visible="menuVisible"
        :menuItems="menuItems"
        :username="username"
      />
    </nav>

    <main class="flex flex-1 items-start justify-center overflow-auto p-8">
      <RouterView />
    </main>
  </div>
</template>
