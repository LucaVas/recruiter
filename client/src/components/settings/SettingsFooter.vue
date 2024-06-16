<script setup lang="ts">
import type { User } from '@/stores/user/schema';
import { formatDate } from '@/utils/dateUtils';
import Button from 'primevue/button';

const { user } = defineProps<{
  user: User;
}>();

defineEmits<{
  (e: 'edit'): void;
}>();
</script>

<template>
  <div class="flex w-full items-center justify-between">
    <div>
      <Button icon="pi pi-user-edit" @click="$emit('edit')" class="md:hidden" outlined />
      <Button
        icon="pi pi-user-edit"
        label="Edit Profile Information"
        @click="$emit('edit')"
        class="hidden md:block"
        outlined
      />
    </div>
    <div>
      <p class="text-md font-semibold">
        Approved on <span class="font-normal">{{ formatDate(user.approvedAt) }}</span>
      </p>
      <p class="text-md font-semibold">
        Created on <span class="font-normal">{{ formatDate(user.createdAt) }}</span>
      </p>
    </div>
  </div>
</template>