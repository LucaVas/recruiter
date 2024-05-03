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
      <div v-if="!loadingComments" id="commentsContainer">
        <div v-if="comments.length > 0">
          <CommentPanel v-for="comment in commentz" :key="comment.id" :comment="comment" />
        </div>
        <div v-else>
          <p>No comments yet.</p>
        </div>
      </div>
      <ProgressSpinner v-else />

      <NewCommentContainer @sendComment="(comment) => send(comment)" />
    </div>

    <template #footer>
      <Button label="Close" severity="secondary" @click="$emit('close')" />
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import Dialog from 'primevue/dialog';
import CommentPanel from './CommentPanel.vue';
import NewCommentContainer from '@/components/comments/shared/NewCommentContainer.vue';
import ProgressSpinner from 'primevue/progressspinner';
import type { CandidacyComment } from '@/stores/candidacy/schema';
import { ref } from 'vue';
import { useToast } from 'primevue/usetoast';

const toast = useToast();

const { visible, comments, loadingComments } = defineProps<{
  visible: boolean;
  comments: CandidacyComment[];
  loadingComments: boolean;
}>();

const commentz = ref(comments);
const sendingComment = ref(false);

const send = async (comment: string) => {
  sendingComment.value = true;
  try {
    console.log('Seding comment:', comment);
    // await addCandidacyComment(jobId, pan, comment);
    emits('loadComments');
  } catch (e) {
    toast.add({ severity: 'error', summary: 'Error', detail: `Failed to send comment: ${e}` });
  } finally {
    sendingComment.value = false;
  }
};

const emits = defineEmits<{
  (e: 'close'): void;
  (e: 'loadComments'): void;
}>();
</script>
