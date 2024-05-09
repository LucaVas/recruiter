<script setup lang="ts">
import DataTable, { type DataTableRowReorderEvent } from 'primevue/datatable';
import Column from 'primevue/column';
import { ref } from 'vue';
import SkillsDropdown from '@/components/job/shared/SkillsDropdown.vue';
import type { Skill } from '@/stores/skill/schema';

const { skills, disabled } = defineProps<{
  skills: Skill[];
  disabled: boolean;
}>();

const onRowReorder = (event: DataTableRowReorderEvent) => {
  details.value = event.value;
};

const removeSkill = (skill: Skill): void => {
  if (!details.value.includes(skill)) return;
  details.value.splice(details.value.indexOf(skill), 1);
  emits('update', details.value);
};

const addSkill = (skill: Skill): void => {
  if (details.value.some((s) => s.name === skill.name)) return;
  details.value.unshift(skill);

  emits('update', details.value);
};

const emits = defineEmits<{
  (e: 'update', content: Skill[]): void;
}>();

const details = ref(skills);
</script>

<template>
  <div class="flex flex-col gap-2">
    <label for="skills">Skills</label>
    <SkillsDropdown :skills="skills" :disabled="disabled" @addSkill="(skill) => addSkill(skill)" />
    <DataTable
      scrollable
      scrollHeight="15rem"
      :value="details"
      :reorderableColumns="true"
      size="small"
      class="mb-2"
      @rowReorder="onRowReorder"
    >
      <Column rowReorder headerStyle="width: 3rem" :reorderableColumn="true" />
      <Column field="name" style="width: 90%"></Column>
      <Column style="flex: 0 0 4rem">
        <template #body="{ data }">
          <Button
            type="button"
            icon="pi pi-times"
            text
            size="small"
            @click="removeSkill(data)"
            :disabled="disabled"
          />
        </template>
      </Column>
    </DataTable>
  </div>
</template>
