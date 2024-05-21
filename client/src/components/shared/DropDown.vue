<script setup lang="ts">
import { ref } from 'vue';
import InputGroup from 'primevue/inputgroup';
import Dropdown from 'primevue/dropdown';

const { label, model, options } = defineProps<{
  label?: string;
  model: any;
  options: any[];
}>();
const emit = defineEmits<{
  (e: 'select', content: any): void;
}>();
const option = ref(model);
</script>

<template>
  <div class="flex w-full flex-col gap-2">
    <label v-if="label" class="text-sm">{{ label }}</label>
    <InputGroup>
      <Dropdown
        v-model="option"
        :options="options"
        optionLabel="name"
        optionValue="value"
        class="w-full"
        @change="emit('select', option)"
      />
    </InputGroup>
  </div>
</template>
