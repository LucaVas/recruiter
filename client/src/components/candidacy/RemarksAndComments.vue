<script setup lang="ts">
import Textarea from 'primevue/textarea';
import { ref } from 'vue';

const { isArchived } = defineProps<{
  isArchived: boolean;
}>();

const details = ref({
  remarks: '',
  comments: '',
});
const emit = defineEmits<{
  (e: 'input', content: typeof details.value): void;
}>();
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

    <div v-if="details.comments" class="flex w-full flex-col gap-2">
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
    </div>
  </div>
</template>
