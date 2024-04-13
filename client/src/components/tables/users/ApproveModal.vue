<script setup lang="ts">
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import Divider from 'primevue/divider';
import { ref } from 'vue';
import { useToast } from 'primevue/usetoast';

const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 5000 });
};
const { visible } = defineProps<{
  visible: boolean;
}>();
const comments = ref('');
const invalidComments = ref(false);
const error = ref('');

function verifyApproval() {
  if (comments.value === '') {
    invalidComments.value = true;
    showError('Comments are required.');
    return;
  }
  emits('approve', comments.value);
  comments.value = '';
}
const emits = defineEmits<{
  (e: 'approve', comments: string): void;
  (e: 'closeModal'): void;
}>();
</script>

<template>
  <Toast />
  <div class="card flex justify-center">
    <Dialog :visible="visible" modal header="Confirm approval" :style="{ width: '25rem' }">
      <Textarea
        v-model="comments"
        rows="5"
        cols="30"
        placeholder="Write here your comments..."
        class="w-full"
        :invalid="invalidComments"
      />
      <Message v-if="error" severity="error" :closable="false" class="w-full">{{ error }}</Message>
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
          label="Save"
          @click="verifyApproval()"
        ></Button>
      </div>
    </Dialog>
  </div>
</template>
