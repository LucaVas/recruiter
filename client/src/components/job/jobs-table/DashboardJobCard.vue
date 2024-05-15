<template>
  <div class="h-30 flex flex-col items-start gap-4">
    <div id="title" class="space-y-3">
      <h2 class="font-semibold">{{ job.name }}</h2>
      <h4 class="italic">{{ job.client.name }}</h4>
    </div>
    <footer class="text-slate-400">{{ formatDate(job.createdDTime) }}</footer>
  </div>
</template>

<script setup lang="ts">
import type { Job } from '@/stores/job/schema';
import { formatDate } from '@/utils/dateUtils';

const { job } = defineProps<{
  job: Job;
}>();
</script>
