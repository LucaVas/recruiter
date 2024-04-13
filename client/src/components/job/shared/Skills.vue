<script setup lang="ts">
import DataTable, { type DataTableRowReorderEvent } from 'primevue/datatable';
import Column from 'primevue/column';
import { ref } from 'vue';
import SkillsDropdown from '@/components/job/shared/SkillsDropdown.vue';
import type { RawSkill } from '@/stores/skill/types';
import type { Job } from '@/stores/job/types';

const { job, disabled } = defineProps<{
  job: Job;
  disabled: boolean;
}>();

const onRowReorder = (event: DataTableRowReorderEvent) => {
  details.value.skills = new Set(event.value);
};

const removeSkill = (skill: RawSkill): void => {
  details.value.skills.delete(skill);
  emits('update', details.value);
};

const addSkill = (skill: RawSkill): void => {
  details.value.skills.add(skill);
  emits('update', details.value);
};

const emits = defineEmits<{
  (e: 'update', content: Job): void;
}>();

const details = ref(job);
</script>

<template>
  <div class="flex flex-col gap-2">
    <label for="skills">Skills</label>
    <DataTable
      scrollable
      scrollHeight="15rem"
      :value="new Array(details.skills)"
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
    <SkillsDropdown :disabled="disabled" @addSkill="(skill) => addSkill(skill)" />
  </div>
</template>
