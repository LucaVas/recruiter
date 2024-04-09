<script setup lang="ts">
import { ref, onMounted } from 'vue';
import type { SkillDto } from '../../stores/skill/types';
import Dropdown from 'primevue/dropdown';
import { getAllSkills } from '@/stores/skill';

const loading = ref(false);
const skills = ref<SkillDto[]>();
const selectedSkill = ref<SkillDto>();

onMounted(async () => {
  loading.value = true;
  skills.value = await getAllSkills();
  loading.value = false;
});

const { disabled } = defineProps<{
  disabled: boolean;
}>();

defineEmits<{
  (e: 'addSkill', skill: SkillDto): void;
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
      :disabled="disabled"
    >
      <template #value="slotProps">
        <div v-if="slotProps.value" class="align-items-center flex">
          <div>{{ slotProps.value.name }}</div>
        </div>
      </template>
    </Dropdown>
  </div>
</template>
