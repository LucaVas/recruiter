<script setup lang="ts">
import type { Skill } from '@/stores/skill/schema';

import { ref } from 'vue';

const { skills } = defineProps<{
  skills: Skill[];
}>();

const expandedRowGroups = ref();

defineEmits<{
  (e: 'removeSkill', skills: Skill): void;
}>();
</script>

<template>
  <div v-if="skills.length > 0" class="flex flex-col gap-3">
    <div class="flex flex-col gap-2">
      <div>
        <DataTable
          v-model:expandedRowGroups="expandedRowGroups"
          :value="skills"
          tableStyle="min-width: 50rem"
          expandableRowGroups
          rowGroupMode="subheader"
          groupRowsBy="name"
          @rowgroup-expand="console.log('rowgroup-expand', $event)"
          @rowgroup-collapse="console.log('rowgroup-collapse', $event)"
          sortMode="single"
          sortField="name"
          :sortOrder="1"
        >
          <template #groupheader="slotProps">
            <span class="line-height-3 ml-2 font-bold">{{ slotProps.data.name }}</span>
            <Button
              icon="pi pi-trash p-2 hover:bg-slate-100 rounded-md ml-2"
              @click="$emit('removeSkill', slotProps.data)"
              size="small"
              unstyled
            />
          </template>
          <Column field="questionText" style="width: 80%">
            <template #body="slotProps">
              <ul class="list-disc">
                <li v-for="question in slotProps.data.questions" :key="question.id">
                  {{ question.text }}
                </li>
              </ul>
            </template>
          </Column>
        </DataTable>
      </div>
    </div>
  </div>
</template>
