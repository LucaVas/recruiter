<script setup lang="ts">
import type { Skill } from '@/stores/skill/schema';
import Chip from 'primevue/chip';

const { skills, isNewJob } = defineProps<{
  skills: Skill[];
  isNewJob: boolean;
}>();

defineEmits<{ (e: 'remove', skill: Skill): void }>();
</script>

<template>
  <div class="flex w-full flex-wrap gap-2">
    <Chip
      v-for="skill in skills"
      :key="skill.name"
      @remove="$emit('remove', skill)"
      :removable="isNewJob"
      :label="skill.name"
    />
  </div>
</template>
