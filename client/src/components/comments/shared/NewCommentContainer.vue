<template>
  <div id="newCommentContainer" class="w-full">
    <TextArea v-model="comment" rows="4" cols="30" class="w-full" />
  </div>
  <div class="flex justify-end gap-2">
    <Button icon="pi pi-send" label="Send Comment" @click="{ $emit('sendComment', comment); comment = '' }" />
  </div>
</template>

<script setup lang="ts">
import TextArea from 'primevue/textarea';
import { ref } from 'vue';

defineEmits<{
  (e: 'sendComment', comment: string): void;
}>();

const comment = ref('');
</script>
