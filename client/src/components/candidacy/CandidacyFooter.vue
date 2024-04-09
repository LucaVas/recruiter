<script setup lang="ts">
import type { JobStatus } from '@/stores/job/types';
import { useRouter } from 'vue-router';

const router = useRouter()
const { submittingNewCandidate, candidateSubmitted, status } = defineProps<{
  submittingNewCandidate: boolean;
  status: JobStatus;
  candidateSubmitted: boolean;
}>();

defineEmits<{
  (e: 'submit'): void;
}>();
</script>

<template>
  <div class="flex w-full justify-between">
    <Button
        v-if="!candidateSubmitted"
        label="Back"
        size="small"
        :loading="submittingNewCandidate"
        @click="router.go(-1)"
      />
    <div>
      <Button
        v-if="status !== 'ARCHIVED' && !candidateSubmitted"
        label="Submit"
        size="small"
        @click="$emit('submit')"
        :loading="submittingNewCandidate"
      />
      <Button
        v-if="candidateSubmitted"
        label="Back to Dashboard"
        size="small"
        @click="router.push({ name: 'Dashboard' })"
      />
    </div>
  </div>
</template>
