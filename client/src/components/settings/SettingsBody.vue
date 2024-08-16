<template>
  <div class="flex flex-col gap-4">
    <div class="space-y-1">
      <h2 class="text-lg font-bold">Email</h2>
      <p>{{ user.email }}</p>
    </div>
    <div class="space-y-1">
      <h2 class="text-lg font-bold">Phone</h2>
      <p>{{ user.phone }}</p>
    </div>
    <div class="space-y-1">
      <h2 class="text-lg font-bold">Location</h2>
      <p>{{ user.city }}, {{ user.country }}</p>
    </div>
    <div class="space-y-1">
      <h2 class="text-lg font-bold">Role</h2>
      <p>{{ user.roleName === 'ADMIN' ? 'Administrator' : 'Recruiter' }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { User } from '@/stores/user/schema';

const { user } = defineProps<{
  user: User;
}>();
</script>
