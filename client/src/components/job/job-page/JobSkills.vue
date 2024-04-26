<script setup lang="ts">
import type { Skill } from '@/stores/skill/schema';
import Chip from 'primevue/chip';

const { skills } = defineProps<{
  skills: Skill[];
}>();
</script>

<template>
  <h3 class="text-lg font-medium">Skills</h3>
  <div class="flex w-full flex-wrap gap-2">
    <Chip v-for="skill in skills" :key="skill.id" :label="skill.name" />
  </div>
</template>
