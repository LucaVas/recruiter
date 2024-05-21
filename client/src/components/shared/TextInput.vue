<script setup lang="ts">
import { ref } from 'vue';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputText from 'primevue/inputtext';

const { label, icon, model } = defineProps<{
  label: string;
  icon: string;
  model: string;
}>();
const emit = defineEmits<{
  (e: 'input', content: string): void;
}>();
const text = ref(model);
</script>

<template>
  <div class="flex w-full flex-col gap-2">
    <label class="text-sm">{{ label }}</label>
    <InputGroup>
      <InputGroupAddon><i :class="`pi ${icon}`" /></InputGroupAddon>
      <InputText v-model="text" @input="emit('input', text)" />
    </InputGroup>
  </div>
</template>
