<template>
  <div>
    <div class="flex items-center justify-between">
      <div class="flex items-center">
        <Avatar icon="pi pi-user" :name="user.username" />
        <div class="ml-2">
          <div class="text-lg font-semibold">{{ user.username }}</div>
          <div class="text-sm text-gray-500">{{ user.email }}</div>
        </div>
      </div>
      <div class="items flex"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { type User } from '@/stores/user/schema';

const { user } = defineProps<{
  user: User;
}>();
</script>
