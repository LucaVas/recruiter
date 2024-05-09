<template>
  <div class="card">
    <Panel>
      <template #header>
        <div class="align-center flex gap-2">
          <span class="font-bold">{{ comment.author.username }}</span>
        </div>
      </template>
      <template #footer>
        <div class="align-center flex flex-wrap justify-end gap-3">
          <span class="p-text-secondary italic text-slate-400">{{
            formatDateTime(comment.createdDTime)
          }}</span>
        </div>
      </template>
      <p class="m-0">
        {{ comment.text }}
      </p>
    </Panel>
  </div>
</template>

<script setup lang="ts">
import Panel from 'primevue/panel';
import { type CandidacyComment } from '@/stores/candidacy/schema';
import { formatDateTime } from '@/utils/dateUtils';

const { comment } = defineProps<{
  comment: CandidacyComment;
}>();
</script>
