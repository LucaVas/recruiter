<template>
  <div class="flex w-full justify-end">
    <div class="text-lg font-semibold">
      Created on <span class="font-normal">{{ formatDate(user.createdDTime) }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { User } from '@/stores/user/schema';
import { formatDate } from '../../utils/dateUtils';

const { user } = defineProps<{
  user: User;
}>();
</script>
