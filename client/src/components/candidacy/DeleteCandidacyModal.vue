<script setup lang="ts">
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import Divider from 'primevue/divider';

const { visible, deletingCandidacy } = defineProps<{
  visible: boolean;
  deletingCandidacy: boolean;
}>();

defineEmits<{
  (e: 'delete'): void;
  (e: 'close'): void;
}>();
</script>

<template>
  <div class="card flex justify-center">
    <Dialog
      @update:visible="$emit('close')"
      close-on-escape
      :visible="visible"
      modal
      header="Are you sure?"
      :style="{ width: '25rem' }"
    >
      <h3>Are you sure you want to delete this candidacy?</h3>
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
          :loading="deletingCandidacy"
          :disabled="deletingCandidacy"
        />
        <Button
          type="button"
          size="small"
          icon="pi pi-check"
          severity="success"
          label="Delete"
          :loading="deletingCandidacy"
          :disabled="deletingCandidacy"
          @click="$emit('delete')"
        />
      </div>
    </Dialog>
  </div>
</template>
