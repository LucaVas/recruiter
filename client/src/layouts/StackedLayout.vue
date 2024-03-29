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
    <div class="h-full w-[20%]">
      <Navbar :links="links" :role="role" :tag="tag" />
    </div>

    <main class="flex h-screen w-[80%] flex-col items-center justify-start p-4">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
main {
  min-height: 75svh;
}
</style>
