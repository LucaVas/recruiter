<template>
  <div class="h-30 flex flex-col gap-4">
    <header id="header">
      <h2 class="font-semibold">{{ candidate.name }}</h2>
      <h4 class="italic">{{ candidate.email }}</h4>
    </header>

    <div id="body" class="flex flex-col">
      <p class="m-0">Education: {{ candidate.education }}</p>
      <p class="m-0">Phone: {{ candidate.phone }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Candidate } from '@/types/candidateTypes';

const { candidate } = defineProps<{
  candidate: Candidate;
}>();
</script>
