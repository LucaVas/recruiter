<script setup lang="ts">
import { ref } from 'vue';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputText from 'primevue/inputtext';

const { label, icon, model, placeholder } = defineProps<{
  label?: string;
  icon?: string;
  model: string;
  placeholder?: string;
}>();
const emit = defineEmits<{
  (e: 'input', content: string): void;
}>();
const text = ref(model);
</script>

<template>
  <div class="flex w-full flex-col gap-2">
    <label v-if="label" class="text-sm">{{ label }}</label>
    <InputGroup>
      <InputGroupAddon v-if="icon"><i :class="`pi ${icon}`" /></InputGroupAddon>
      <InputText :placeholder="placeholder ?? ''" v-model="text" @update:modelValue="emit('input', text)" />
    </InputGroup>
  </div>
</template>
