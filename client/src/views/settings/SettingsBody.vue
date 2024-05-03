<template>
  <div class="flex flex-col gap-4">
    <div>
      <div class="text-xl font-bold">Email</div>
      <div>{{ user.email }}</div>
    </div>
    <div>
      <div class="text-xl font-bold">Mobile</div>
      <div>{{ user.mobile }}</div>
    </div>
    <div>
      <div class="text-xl font-bold">Location</div>
      <div>{{ user.city }}, {{ user.country }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { User } from '@/stores/user/schema';

const { user } = defineProps<{
  user: User;
}>();
</script>
