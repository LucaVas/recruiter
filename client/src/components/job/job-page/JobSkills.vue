<script setup lang="ts">
import type { SkillDto } from '@/stores/skill/types';
import Chip from 'primevue/chip';

const { skills } = defineProps<{
  skills: SkillDto[];
}>();



</script>

<template>
  <h3 class="text-lg font-medium">Job Skills</h3>
  <div class="flex w-full flex-wrap gap-2">
    <Chip v-for="skill in skills" :key="skill.id" :label="skill.name" />
  </div>
</template>
