<script setup lang="ts">
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import Divider from 'primevue/divider';
import { ref } from 'vue';

const { visible } = defineProps<{
  visible: boolean;
}>();
const comment = ref('');
const error = ref('');

defineEmits<{
  (e: 'approve', comment: string): void;
  (e: 'close'): void;
}>();
</script>

<template>
  <div class="card flex justify-center">
    <Dialog :visible="visible" modal header="Confirm approval" :style="{ width: '20rem' }">
      <Textarea
        v-model="comment"
        rows="5"
        cols="30"
        placeholder="Write here your comments..."
        class="w-full"
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
          @click="$emit('close')"
        ></Button>
        <Button
          type="button"
          size="small"
          icon="pi pi-check"
          severity="success"
          label="Save"
          @click="$emit('approve', comment)"
        ></Button>
      </div>
    </Dialog>
  </div>
</template>
