<script setup lang="ts">
import Button from 'primevue/button';
import NewCandidateModal from './new-candidate/NewCandidateModal.vue';
import { ref } from 'vue';
import type { CandidateDto } from '@/stores/candidate/types';

const emits = defineEmits<{
  (e: 'passError', content: string): void;
  (e: 'passNewCandidate', candidate: CandidateDto): void;
}>();

const newCandidateModalOpen = ref(false);

function prePass(newCandidate: CandidateDto) {
  newCandidateModalOpen.value = false;
  emits('passNewCandidate', newCandidate);
}
</script>

<template>
  <div>
    <div class="field p-fluid">
      <Button
        label="New candidate"
        icon="pi pi-user-plus"
        iconPos="right"
        outlined
        @click="newCandidateModalOpen = true"
      />
    </div>

    <NewCandidateModal
      v-if="newCandidateModalOpen"
      @close="newCandidateModalOpen = false"
      @selectCandidate="(newCandidate: CandidateDto) => prePass(newCandidate)"
    />
  </div>
</template>
