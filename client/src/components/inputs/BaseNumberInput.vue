<script setup lang="ts">
import { ref } from 'vue';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputNumber from 'primevue/inputnumber';

const { label, icon, model, min, max, leading, trailing } = defineProps<{
  label?: string;
  icon?: string;
  model: number;
  min?: number;
  max?: number;
  trailing?: string;
  leading?: string;
}>();
defineEmits<{
  (e: 'input', content: number): void;
}>();
const number = ref(model);
</script>

<template>
  <div class="flex w-full flex-col gap-2">
    <label v-if="label" class="text-sm">{{ label }}</label>
    <InputGroup>
      <InputGroupAddon v-if="icon"><i :class="`pi ${icon}`" /></InputGroupAddon>
      <InputGroupAddon v-if="leading" class="min-w-fit">{{ leading }}</InputGroupAddon>
      <InputNumber
        v-model="number"
        :min="min"
        :max="max"
        @update:modelValue="$emit('input', number)"
      />
      <InputGroupAddon v-if="trailing" class="min-w-fit">{{ trailing }}</InputGroupAddon>
    </InputGroup>
  </div>
</template>
