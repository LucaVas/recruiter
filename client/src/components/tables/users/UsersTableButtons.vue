<template>
  <div class="flex gap-2">
    <div v-if="!user.approved">
      <Button
        icon="pi pi-check"
        severity="success"
        class="md:hidden"
        size="small"
        outlined
        @click="$emit('openModal', true)"
      />
      <Button
        icon="pi pi-check"
        label="Approve"
        severity="success"
        class="hidden w-36 md:flex"
        size="small"
        rounded
        outlined
        @click="$emit('openModal', true)"
      />
    </div>

    <div v-else>
      <Button
        icon="pi pi-times"
        severity="danger"
        size="small"
        outlined
        class="md:hidden"
        @click="$emit('openModal', false)"
      />
      <Button
        icon="pi pi-times"
        severity="danger"
        label="Reject"
        rounded
        outlined
        class="hidden w-36 md:flex"
        size="small"
        @click="$emit('openModal', false)"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { type User } from '@/stores/user/schema';

const { user } = defineProps<{
  user: User;
}>();

defineEmits<{
  (e: 'openModal', isApproval: boolean): void;
}>();
</script>
