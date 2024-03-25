<script lang="ts" setup>
import Navbar from '@/components/Navbar.vue';
import type { MenuItem } from 'primevue/menuitem';

const { links, tag } = defineProps<{
  links: MenuItem[];
  tag: string;
  role: string;
}>();
</script>

<template>
  <div class="flex h-screen w-screen items-center justify-center">
    <Navbar :links="links" :role="role" :tag="tag" />

    <main class="flex h-full w-full flex-col items-center justify-center p-6">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
main {
  min-height: 75svh;
}
</style>
