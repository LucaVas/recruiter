<script setup lang="ts">
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import Divider from 'primevue/divider';
import { ref } from 'vue';

const { visible, isApproval } = defineProps<{
  visible: boolean;
  isApproval: boolean
}>();
const comment = ref('');

defineEmits<{
  (e: 'approve', comment: string): void;
  (e: 'close'): void;
}>();
</script>

<template>
  <div class="card flex justify-center">
    <Dialog :visible="visible" modal :header="`Confirm ${isApproval ? 'approval' : 'rejection'}`">
      <Textarea
        v-model="comment"
        rows="5"
        cols="30"
        placeholder="Write here your comment"
        class="mt-3 w-full"
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
          @click="
            {
              $emit('close');
              comment = '';
            }
          "
        ></Button>
        <Button
          type="button"
          size="small"
          icon="pi pi-check"
          severity="success"
          label="Save"
          @click="
            {
              $emit('approve', comment);
              comment = '';
            }
          "
        ></Button>
      </div>
    </Dialog>
  </div>
</template>
