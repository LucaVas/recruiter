<template>
  <div class="card">
    <div class="flex flex-col gap-1">
      <span class="self-start font-semibold">{{ comment.author.name }}</span>
      <div class="ml-5 rounded-md bg-slate-200 p-3">{{ comment.text }}</div>
      <span class="self-end text-sm italic text-slate-400">{{
        formatDateTime(comment.createdAt)
      }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { type CandidacyComment } from '@/stores/candidacy/schema';
import { formatDateTime } from '@/utils/dateUtils';

const { comment } = defineProps<{
  comment: CandidacyComment;
}>();
</script>
