<template>
  <div>
    <Panel toggleable>
      <template #header>
        <div class="align-center flex gap-2">
          <span class="font-bold">{{ question.text }} </span>
        </div>
      </template>
      <template #icons>
        <button class="p-panel-header-icon p-link mr-2" @click="toggle">
          <span class="pi pi-cog"></span>
        </button>
        <Menu ref="menu" id="config_menu" :model="items" popup />
      </template>
      <p class="m-0">
        {{ question.answer }}
      </p>
      <template #footer>
        <div>
          <span class="p-text-secondary">Division: {{ question.division ?? '-' }}</span>
        </div>
      </template>
    </Panel>
  </div>
</template>

<script setup lang="ts">
import Panel from 'primevue/panel';
import Button from 'primevue/button';
import Menu from 'primevue/menu';
import { Question } from '../../stores/question/schema';
import { ref } from 'vue';

const { question } = defineProps<{
  question: Question;
}>();

const menu = ref(null);

const toggle = (event) => {
  if (menu.value) menu.value.toggle(event);
};
const items = ref([
  {
    label: 'Refresh',
    icon: 'pi pi-refresh',
  },
  {
    label: 'Search',
    icon: 'pi pi-search',
  },
  {
    separator: true,
  },
  {
    label: 'Delete',
    icon: 'pi pi-times',
  },
]);
</script>
