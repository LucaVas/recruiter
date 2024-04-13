<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();
const { visible, saved, saving, isUpdate } = defineProps<{
  visible: boolean;
  saving: boolean;
  saved: boolean;
  isUpdate: boolean
}>();

defineEmits<{
  (e: 'save'): void;
}>();
</script>

<template>
  <div class="flex w-full justify-between">
    <Button
      v-if="!saved"
      label="Back"
      size="small"
      :loading="saving"
      @click="router.go(-1)"
    />
    <div>
      <Button v-if="!saved" :label="isUpdate ? 'Update Job' : 'Create Job'" @click="$emit('save')" />
      <Button
        v-if="saved"
        label="Back to Dashboard"
        size="small"
        @click="router.push({ name: 'Dashboard' })"
      />
    </div>
  </div>
</template>
