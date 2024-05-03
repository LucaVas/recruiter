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
      <div v-if="!loadingComments" id="commentsContainer" class="py-3">
        <div v-if="comments.length > 0" class="flex flex-col gap-3 overflow-y-auto max-h-[13rem]">
          <CommentPanel v-for="comment in comments" :key="comment.id" :comment="comment" />
        </div>
        <div v-else>
          <p>No comments yet.</p>
        </div>
      </div>
      <ProgressSpinner v-else />

      <NewCommentContainer @sendComment="(comment) => $emit('send', comment)" />
    </div>
  </Dialog>
</template>

<script setup lang="ts">
import Dialog from 'primevue/dialog';
import CommentPanel from './CommentPanel.vue';
import NewCommentContainer from '@/components/comments/shared/NewCommentContainer.vue';
import ProgressSpinner from 'primevue/progressspinner';
import type { CandidacyComment } from '@/stores/candidacy/schema';

const { visible, comments, loadingComments } = defineProps<{
  visible: boolean;
  comments: CandidacyComment[];
  loadingComments: boolean;
  sendingComment: boolean;
}>();

defineEmits<{
  (e: 'close'): void;
  (e: 'send', comment: string): void;
}>();
</script>
