<script setup lang="ts">
import { ref } from 'vue';
import type { Skill } from '@/stores/skill/schema';
import Dropdown from 'primevue/dropdown';

// variables
const loading = ref(false);
const selectedSkill = ref<Skill>();

// props
const { skills } = defineProps<{
  skills: Skill[];
}>();

// emits
defineEmits<{
  (e: 'addSkill', skill: Skill): void;
}>();
</script>

<template>
  <div class="card justify-content-center flex">
    <Dropdown
      :loading="loading"
      @update:modelValue="(e) => $emit('addSkill', e)"
      v-model="selectedSkill"
      :options="skills"
      filter
      optionLabel="name"
      placeholder="Select a Skill"
      class="w-full"
    >
      <template #value="slotProps">
        <div v-if="slotProps.value" class="align-items-center flex">
          <div>{{ slotProps.value.name }}</div>
        </div>
      </template>
    </Dropdown>
  </div>
</template>
