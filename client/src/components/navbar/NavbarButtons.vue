<template>
  <div class="hidden justify-between gap-3 md:flex md:flex-col">
    <div class="flex flex-col gap-3">
      <Button
        v-for="button in filteredButtons"
        :key="filteredButtons.indexOf(button)"
        :icon="button.icon"
        outlined
        @click="button.command"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import Button from 'primevue/button';
import { useRouter } from 'vue-router';
import { computed, ref } from 'vue';
import useAuthStore from '@/stores/authStore';

const authStore = useAuthStore();
const router = useRouter();
const buttons = ref([
  {
    icon: 'pi pi-home',
    visible: true,
    command: () => router.push({ name: 'Dashboard' }),
  },
  {
    icon: 'pi pi-users',
    visible: authStore.isAdmin,
    command: () => router.push({ name: 'UsersView' }),
  },
  {
    icon: 'pi pi-plus',
    visible: authStore.isAdmin,
    command: () => router.push({ name: 'NewJob' }),
  },
  {
    icon: 'pi pi-users',
    visible: !authStore.isAdmin,
    command: () => router.push({ name: 'CandidatesPage' }),
  },
  {
    icon: 'pi pi-file',
    visible: !authStore.isAdmin,
    command: () => router.push({ name: 'CandidaciesPage' }),
  },
  {
    icon: 'pi pi-cog',
    visible: true,
    command: () => router.push({ name: 'Settings' }),
  },
]);

const filteredButtons = computed(() => buttons.value.filter((button) => button.visible));
</script>
