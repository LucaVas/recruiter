<script setup lang="ts">
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import Divider from 'primevue/divider';
import { ref } from 'vue';

const { visible } = defineProps<{
  visible: boolean;
}>();
const comments = ref('');

defineEmits<{
  (e: 'continueSignup', comments: string): void;
  (e: 'closeModal'): void;
}>();
</script>

<template>
  <div class="card flex justify-center">
    <Dialog :visible="visible" modal header="Your Comments" :style="{ width: '25rem' }">
      <Textarea
        v-model="comments"
        rows="5"
        cols="30"
        placeholder="Write here your comments for the approvers (optional)"
        class="w-full"
      />
      <Divider />
      <div class="flex justify-end gap-2">
        <Button
          type="button"
          label="Cancel"
          severity="danger"
          size="small"
          icon="pi pi-times"
          outlined
          @click="$emit('closeModal')"
        ></Button>
        <Button
          type="button"
          size="small"
          icon="pi pi-check"
          severity="success"
          label="Signup"
          @click="$emit('continueSignup', comments)"
        ></Button>
      </div>
    </Dialog>
  </div>
</template>
