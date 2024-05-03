<template>
  <Dialog
    :visible="visible"
    modal
    header="Comments"
    @update:visible="$emit('close')"
    :style="{ width: '50rem' }"
    :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
  >
    <div class="flex flex-col gap-4">
      <div id="commentsContainer">
        <div v-if="comments.length > 0">
          <CommentPanel v-for="comment in comments" :key="comment.id" :comment="comment" />
        </div>
        <div v-else>
          <p>No comments yet.</p>
        </div>
      </div>

      <div id="newCommentContainer" class="w-full">
        <Textarea v-model="commentText" variant="filled" rows="4" cols="30" class="w-full" />
      </div>
      <div class="flex justify-end gap-2">
        <Button icon="pi pi-send" label="Send Comment" @click="null" />
      </div>
    </div>

    <template #footer>
      <Button label="Close" severity="secondary" @click="$emit('close')" />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import Dialog from 'primevue/dialog';
import CommentPanel from './CommentPanel.vue';
import Textarea from 'primevue/textarea';
import type { CandidacyComment } from '@/stores/candidacy/schema';
import { ref } from 'vue';

const { visible, comments } = defineProps<{
  visible: boolean;
  comments: CandidacyComment[];
}>();

const commentText = ref('');

defineEmits<{
  (e: 'close'): void;
}>();
</script>
