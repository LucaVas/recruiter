<template>
  <div class="hidden gap-3 md:flex md:flex-col">
    <Button
      v-for="button in filteredButtons"
      :key="filteredButtons.indexOf(button)"
      :icon="button.icon"
      outlined
      @click="button.command"
    />
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { isAdmin } from '@/stores/auth/index';
import { computed, ref } from 'vue';

const router = useRouter();
const buttons = ref([
  {
    icon: 'pi pi-home',
    visible: true,
    command: () => router.push({ name: 'Dashboard' }),
  },
  {
    icon: 'pi pi-users',
    visible: isAdmin,
    command: () => router.push({ name: 'UsersView' }),
  },
  {
    icon: 'pi pi-plus',
    visible: isAdmin,
    command: () => router.push({ name: 'NewJob' }),
  },
  {
    icon: 'pi pi-users',
    visible: !isAdmin,
    command: () => router.push({ name: 'CandidatesPage' }),
  },
  {
    icon: 'pi pi-file',
    visible: !isAdmin,
    command: () => router.push({ name: 'CandidaciesPage' }),
  },
]);

const filteredButtons = computed(() => buttons.value.filter((button) => button.visible));
</script>
