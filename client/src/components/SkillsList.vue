<script setup lang="ts">
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import IconField from 'primevue/iconfield';
import type { RawSkill } from '../stores/skill/types';
import { FilterMatchMode } from 'primevue/api';
import { useSkillsStore } from '../stores/skill/index';

const skillsStore = useSkillsStore();
const skills = ref<RawSkill[]>();
const selectedSkills = ref<RawSkill[]>();
const loading = ref(false);
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

onMounted(async () => {
  loading.value = true;
  skills.value = await skillsStore.getAllSkills();
  loading.value = false;
});

const emit = defineEmits<{
  (e: 'updateSelection', skills: RawSkill[]): void
}>()
function updateSelection() {
  if (selectedSkills.value) emit('updateSelection', selectedSkills.value)
}

</script>

<template>
  <DataTable
    v-model:selection="selectedSkills"
    :value="skills"
    v-on:update:selection="updateSelection()"
    selectionMode="multiple"
    dataKey="id"
    paginator
    showGridlines
    :rows="4"
    size="small"
    :loading="loading"
    tableStyle="min-width: 50rem"
    :globalFilterFields="['name']"
  >
    <template #header>
      <div class="flex justify-end">
        <IconField iconPosition="left">
          <InputIcon>
            <i class="pi pi-search" />
          </InputIcon>
          <InputText v-model="filters['global'].value" placeholder="Keyword Search" />
        </IconField>
      </div>
    </template>
    <template #empty> No skills found. </template>
    <template #loading> Loading skills. Please wait. </template>
    <Column field="name" header="Name" style="min-width: 12rem">
      <template #body="{ data }">
        {{ data.name }}
      </template>
    </Column>
  </DataTable>
</template>
