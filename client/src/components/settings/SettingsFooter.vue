<script setup lang="ts">
import type { User } from '@/types/userTypes';
import { formatDate } from '@/utils/dateUtils';
import Button from 'primevue/button';
import ChangePasswordModal from '@/components/modals/ChangePasswordModal.vue';
import { ref } from 'vue';

const { user } = defineProps<{
  user: User;
}>();

defineEmits<{
  (e: 'edit'): void;
}>();

const changePasswordModalOpen = ref(false);
</script>

<template>
  <ChangePasswordModal
    :visible="changePasswordModalOpen"
    @close="changePasswordModalOpen = false"
  />
  <div class="flex w-full items-center justify-between">
    <div class="flex items-center gap-3">
      <Button
        icon="pi pi-user-edit"
        @click="$emit('edit')"
        class="md:hidden"
        outlined
        size="small"
      />
      <Button
        icon="pi pi-user-edit"
        label="Edit Profile Information"
        @click="$emit('edit')"
        class="hidden min-w-fit md:flex"
        size="small"
        outlined
      />
      <div>
        <Button
          icon="pi pi-key"
          size="small"
          outlined
          class="md:hidden"
          @click="changePasswordModalOpen = true"
        />
        <Button
          icon="pi pi-key"
          label="Change Password"
          outlined
          class="hidden min-w-fit md:flex"
          size="small"
          @click="changePasswordModalOpen = true"
        />
      </div>
    </div>
    <div>
      <p class="text-sm font-semibold">
        Approved on <span class="font-normal">{{ formatDate(user.approvedAt) }}</span>
      </p>
      <p class="text-sm font-semibold">
        Created on <span class="font-normal">{{ formatDate(user.createdAt) }}</span>
      </p>
    </div>
  </div>
</template>
