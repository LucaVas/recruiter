<template>
  <div class="flex w-full justify-end">
    <div>
      <p class="text-md font-semibold">
        Approved on <span class="font-normal">{{ formatDate(user.approvedDTime) }}</span>
      </p>
      <p class="text-md font-semibold">
        Created on <span class="font-normal">{{ formatDate(user.createdDTime) }}</span>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { User } from '@/stores/user/schema';
import { formatDate } from '@/utils/dateUtils';

const { user } = defineProps<{
  user: User;
}>();
</script>
