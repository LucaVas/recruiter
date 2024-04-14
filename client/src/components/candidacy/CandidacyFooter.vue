<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();
const { submittingCandidacy, candidacySubmitted, disabled, isUpdate } = defineProps<{
  submittingCandidacy: boolean;
  disabled: boolean;
  candidacySubmitted: boolean;
  isUpdate: boolean;
}>();

defineEmits<{
  (e: 'submit'): void;
  (e: 'update'): void;
}>();
</script>

<template>
  <div class="flex w-full justify-between">
    <Button
      v-if="!candidacySubmitted"
      label="Back"
      size="small"
      :loading="submittingCandidacy"
      @click="router.go(-1)"
    />
    <div>
      <Button
        v-if="!disabled && !candidacySubmitted"
        :label="isUpdate ? 'Update' : 'Submit'"
        size="small"
        @click="isUpdate ? $emit('update') : $emit('submit')"
        :loading="submittingCandidacy"
      />
      <Button
        v-if="candidacySubmitted"
        label="Back to Dashboard"
        size="small"
        @click="router.push({ name: 'Dashboard' })"
      />
    </div>
  </div>
</template>
