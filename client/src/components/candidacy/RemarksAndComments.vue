<script setup lang="ts">
import Textarea from 'primevue/textarea';
import { ref } from 'vue';

const { isArchived } = defineProps<{
  isArchived: boolean;
}>();

const candidacyDetails = ref({
  remarks: '',
  comments: '',
});
</script>

<template>
  <div class="flex flex-col gap-8">

    <div class="flex w-full flex-col gap-2">
      <label for="wantedCvs">Candidacy Remarks</label>
      <div class="field p-fluid flex w-full">
        <Textarea
          v-model="candidacyDetails.remarks"
          class="w-full"
          rows="4"
          cols="30"
          placeholder="Your remarks (optional)"
        />
      </div>
    </div>

    <div class="flex w-full flex-col gap-2">
      <label for="wantedCvs">Admin Comments</label>
      <div class="field p-fluid flex w-full">
        <Textarea
          v-model="candidacyDetails.comments"
          class="w-full"
          rows="4"
          cols="30"
          placeholder="Admin comments"
        />
      </div>
    </div>

  </div>
</template>
