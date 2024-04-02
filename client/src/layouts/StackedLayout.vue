<script lang="ts" setup>
import Navbar from '@/components/Navbar.vue';
import type { MenuItem } from './DashboardLayout.vue';

const { menuItems, tag } = defineProps<{
  menuItems: MenuItem[];
  tag: string;
  role: string;
}>();
</script>

<template>
  <div class="flex h-screen w-screen items-center justify-between">
    <div
      class="flex h-screen min-w-[4rem] items-start justify-center border border-r-slate-400 bg-slate-100 py-2"
    >
      <Navbar :menuItems="menuItems" :role="role" :tag="tag" />
    </div>

    <main class="h-screen w-full p-6">
      <RouterView />
    </main>
  </div>
</template>
