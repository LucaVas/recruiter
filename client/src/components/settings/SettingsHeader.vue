<template>
  <div class="flex w-full flex-col items-center gap-4 md:flex-row md:gap-8">
    <Avatar
      image="https://primefaces.org/cdn/primevue/images/avatar/amyelsner.png"
      class="h-32 w-32 md:w-36"
      size="xlarge"
      shape="circle"
    />
    <div class="flex w-full flex-row items-center justify-center md:justify-start">
      <div class="text-2xl font-bold">{{ user.name }}</div>
      <Divider layout="vertical" />
      <Tag
        :value="user.approved ? 'Approved' : 'Not Approved'"
        :severity="user.approved ? 'success' : 'danger'"
        class="ml-2"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import Divider from 'primevue/divider';
import Tag from 'primevue/tag';
import Avatar from 'primevue/avatar';
import type { User } from '@/stores/user/schema';

const { user } = defineProps<{
  user: User;
}>();
</script>
