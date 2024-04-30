<template>
  <div>
    <Panel toggleable>
      <template #header>
        <div class="align-center flex gap-2">
          <Checkbox
            v-model="questionSelected"
            @change="$emit('selectOrUnselectQuestion', question)"
            name="questionSelected"
            binary
            variant="filled"
          />
          <span class="font-bold">{{ question.text }} </span>
        </div>
      </template>
      <template #icons>
        <Button icon="pi pi-cog" class="p-panel-header-icon p-link mr-2" @click="toggle" unstyled />
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
import Checkbox from 'primevue/checkbox';
import { type Question } from '../../stores/question/schema';
import { ref } from 'vue';

const { question } = defineProps<{
  question: Question;
}>();

defineEmits<{
  (event: 'selectOrUnselectQuestion', question: Question): void;
}>();

const questionSelected = ref();
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
