<script setup lang="ts">
import type { Skill } from '@/stores/skill/types';
import Tree from 'primevue/tree';
import { computed } from 'vue';

const { skills, disabled } = defineProps<{
  skills: Skill[];
  disabled: boolean;
}>();

const nodes = computed(() => {
  return skills.map((skill) => ({
    key: skill.id,
    label: skill.name,
    children: skill.questions.map((question) => ({
      key: question.id,
      label: question.text,
    })),
  }));
});
</script>

<template>
  <div v-if="nodes.length > 0" class="card justify-content-center flex">
    <Tree :value="nodes" class="md:w-30rem w-full"></Tree>
  </div>
</template>
