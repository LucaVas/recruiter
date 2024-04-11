<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();
const { visible, jobUpdated, updatingJob } = defineProps<{
  visible: boolean;
  updatingJob: boolean;
  jobUpdated: boolean;
}>();

defineEmits<{
  (e: 'update'): void;
}>();
</script>

<template>
  <div class="flex w-full justify-between">
    <Button
      v-if="!jobUpdated"
      label="Back"
      size="small"
      :loading="updatingJob"
      @click="router.go(-1)"
    />
    <div>
      <Button v-if="visible" label="Update Job" @click="$emit('update')" />
      <Button
        v-if="jobUpdated"
        label="Back to Dashboard"
        size="small"
        @click="router.push({ name: 'Dashboard' })"
      />
    </div>
  </div>
</template>
