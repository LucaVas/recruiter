<script setup lang="ts">
import Button from 'primevue/button';
import type { JobStatus } from '@/stores/job/types';
import { useRouter } from 'vue-router';
import { isAdmin } from '@/stores/auth';

const router = useRouter();
const { id, status } = defineProps<{
  id: number;
  status: JobStatus;
}>();
defineEmits<{
  (e: 'openModal'): void;
  (e: 'deleteJob'): void;
}>();
</script>

<template>
  <div class="flex w-full items-center sm:justify-normal justify-around gap-2">
    <Button
      type="button"
      size="small"
      label="Apply"
      v-if="status !== 'ARCHIVED'"
      @click="router.push({ name: 'NewCandidacy', params: { id: id } })"
    />
    <div>
      <Button
        rounded
        type="button"
        size="small"
        label="View Details"
        outlined
        class="hidden sm:block"
        v-if="status !== 'ARCHIVED'"
        @click="$emit('openModal')"
      />
      <Button
        type="button"
        size="small"
        icon="pi pi-eye"
        outlined
        class="block sm:hidden min-w-max"
        v-if="status !== 'ARCHIVED'"
        @click="$emit('openModal')"
      />
    </div>

    <div>
      <Button
        rounded
        type="button"
        size="small"
        severity="warning"
        label="Edit"
        outlined
        class="hidden sm:block"
        v-if="status !== 'ARCHIVED' && isAdmin"
        @click="router.push({ name: 'UpdateJob', params: { id: id } })"
      />
      <Button
        type="button"
        size="small"
        severity="warning"
        icon="pi pi-file-edit"
        outlined
        class="visible sm:hidden min-w-max"
        v-if="status !== 'ARCHIVED' && isAdmin"
        @click="router.push({ name: 'UpdateJob', params: { id: id } })"
      />
    </div>
    <div>
      <Button
        rounded
        type="button"
        size="small"
        severity="danger"
        label="Delete"
        outlined
        class="hidden sm:block"
        v-if="status !== 'ARCHIVED' && isAdmin"
        @click="$emit('deleteJob')"
      />
      <Button
        type="button"
        size="small"
        severity="danger"
        icon="pi pi-trash"
        outlined
        class="block sm:hidden min-w-max"
        v-if="status !== 'ARCHIVED' && isAdmin"
        @click="$emit('deleteJob')"
      />
    </div>
  </div>
  <Divider />
</template>
