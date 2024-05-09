<template>
  <div class="h-30 flex flex-col items-start gap-4">
    <div id="title">
      <h2 class="font-semibold">{{ candidacy.job.name }}</h2>
      <h4 class="italic">{{ candidacy.job.client.name }}</h4>
    </div>
    <footer class="text-slate-400">{{ formatDate(candidacy.createdDTime) }}</footer>
  </div>
</template>

<script setup lang="ts">
import type { Candidacy } from '@/stores/candidacy/schema';
import { formatDate } from '@/utils/dateUtils';

const { candidacy } = defineProps<{
  candidacy: Candidacy;
}>();
</script>
