<script setup lang="ts">
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import Divider from 'primevue/divider';

const { visible } = defineProps<{
  visible: boolean;
}>();

defineEmits<{
  (e: 'deleteJob'): void;
  (e: 'closeModal'): void;
}>();
</script>

<template>
  <div class="card flex justify-center">
    <Dialog :visible="visible" modal header="Are you sure?" :style="{ width: '25rem' }">
      <h3>Please, confirm this job deletion.</h3>
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
          label="Delete"
          @click="$emit('deleteJob')"
        ></Button>
      </div>
    </Dialog>
  </div>
</template>
