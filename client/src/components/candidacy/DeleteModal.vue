<script setup lang="ts">
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import Divider from 'primevue/divider';

const { visible, deleting, message } = defineProps<{
  visible: boolean;
  deleting: boolean;
  message: string;
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
      <h3>{{ message }}</h3>
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
          :loading="deleting"
          :disabled="deleting"
        />
        <Button
          type="button"
          size="small"
          icon="pi pi-check"
          severity="success"
          label="Confirm"
          :loading="deleting"
          :disabled="deleting"
          @click="$emit('delete')"
        />
      </div>
    </Dialog>
  </div>
</template>
