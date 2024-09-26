<script setup lang="ts">
import type { RawCandidacy } from '@/types/candidacyTypes';
import Textarea from 'primevue/textarea';
import { ref } from 'vue';

const { candidacy } = defineProps<{
  candidacy: RawCandidacy;
}>();

const emit = defineEmits<{
  (e: 'input', content: RawCandidacy): void;
}>();

const details = ref(candidacy);
</script>

<template>
  <div class="flex flex-col gap-8">
    <div class="flex w-full flex-col gap-2">
      <label class="text-sm" for="wantedCvs">Comments</label>
      <div class="flex w-full">
        <Textarea
          v-model="details.recruiterComment"
          @input="emit('input', details)"
          class="w-full"
          rows="4"
          cols="30"
          placeholder="Your comments (optional)"
        />
      </div>
    </div>
  </div>
</template>
