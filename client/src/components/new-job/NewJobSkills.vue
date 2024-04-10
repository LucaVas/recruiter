<script setup lang="ts">
import DataTable, { type DataTableRowReorderEvent } from 'primevue/datatable';
import Column from 'primevue/column';
import { ref, onMounted } from 'vue';
import NewJobSkillsDropdown from './NewJobSkillsDropdown.vue';
import type { SkillDto } from '../../stores/skill/types';

const { skills } = defineProps<{
  skills: SkillDto[];
}>();
const updatedSkills = ref<SkillDto[]>([]);

const onRowReorder = (event: DataTableRowReorderEvent) => {
  updatedSkills.value = event.value;
};

defineEmits<{
  (e: 'removeSkill', skill: SkillDto): void;
}>();

onMounted(() => {
  updatedSkills.value = skills;
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <label for="skills">Skills</label>
    <DataTable
      scrollable
      scrollHeight="15rem"
      :value="skills"
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
            @click="$emit('removeSkill', data)"
          
          />
        </template>
      </Column>
    </DataTable>
    <NewJobSkillsDropdown @addSkill="(skill) => updatedSkills.push(skill)" />
  </div>
</template>
