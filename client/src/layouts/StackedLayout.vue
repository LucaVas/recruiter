<script lang="ts" setup>
import Navbar from '@/components/navbar/Navbar.vue';
import type { MenuItem } from './DashboardLayout.vue';
import { ref } from 'vue';

const { menuItems, tag } = defineProps<{
  menuItems: MenuItem[];
  tag: string;
  role: string;
}>();
const menuVisible = ref(false);
</script>

<template>
  <div class="flex h-screen w-screen items-center justify-between">
    <nav
      class="flex h-full min-w-[5rem] items-start justify-center border border-r-slate-400 bg-slate-100 py-2"
    >
      <Button icon="pi pi-bars" @click="menuVisible = true" />
      <Navbar
        @closeMenu="menuVisible = false"
        :visible="menuVisible"
        :menuItems="menuItems"
        :role="role"
        :tag="tag"
      />
    </nav>

    <main class="h-full max-w-[85rem] p-8">
      <RouterView />
    </main>
  </div>
</template>
