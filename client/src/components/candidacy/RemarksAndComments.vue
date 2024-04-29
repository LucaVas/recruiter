<script setup lang="ts">
import type { RawCandidacy } from '@/stores/candidacy/schema';
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
      <label class="text-sm" for="wantedCvs">Candidacy Remarks</label>
      <div class="flex w-full">
        <Textarea
          v-model="details.remarks"
          @input="emit('input', details)"
          class="w-full"
          rows="4"
          cols="30"
          placeholder="Your remarks (optional)"
        />
      </div>
    </div>

    <!-- <div v-if="details.comments" class="flex w-full flex-col gap-2">
      <label class="text-sm" for="wantedCvs">Admin Comments</label>
      <div class="flex w-full">
        <Textarea
          v-model="details.comments"
          @input="emit('input', details)"
          class="w-full"
          rows="4"
          cols="30"
          placeholder="Admin comments"
        />
      </div>
    </div> -->
  </div>
</template>
