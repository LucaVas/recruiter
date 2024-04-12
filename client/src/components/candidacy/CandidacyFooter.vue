<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter()
const { submittingNewCandidacy, candidacySubmitted, disabled, isUpdate } = defineProps<{
  submittingNewCandidacy: boolean;
  disabled: boolean;
  candidacySubmitted: boolean;
  isUpdate: boolean
}>();

defineEmits<{
  (e: 'submit'): void;
  (e: 'update'): void
}>();
</script>

<template>
  <div class="flex w-full justify-between">
    <Button
        v-if="!candidacySubmitted"
        label="Back"
        size="small"
        :loading="submittingNewCandidacy"
        @click="router.go(-1)"
      />
    <div>
      <Button
        v-if="!disabled && !candidacySubmitted"
        :label="isUpdate ? 'Update' : 'Submit'"
        size="small"
        @click="isUpdate ? $emit('update') : $emit('submit')"
        :loading="submittingNewCandidacy"
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
