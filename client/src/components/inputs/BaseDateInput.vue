<script setup lang="ts">
import { ref } from 'vue';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import Calendar from 'primevue/calendar';

const { label, icon, model, leading, trailing } = defineProps<{
  label?: string;
  icon?: string;
  model: Date;
  trailing?: string;
  leading?: string;
}>();
const emit = defineEmits<{
  (e: 'select', content: Date): void;
}>();
const date = ref(model);
</script>

<template>
  <div class="flex w-full flex-col gap-2">
    <label v-if="label" class="text-sm">{{ label }}</label>
    <InputGroup>
      <InputGroupAddon v-if="icon"><i :class="`pi ${icon}`" /></InputGroupAddon>
      <InputGroupAddon v-if="leading" class="min-w-fit">{{ leading }}</InputGroupAddon>
      <Calendar
        v-model="date"
        showIcon
        :minDate="new Date()"
        dateFormat="dd/mm/yy"
        iconDisplay="input"
        inputId="icondisplay"
        required
        @update:modelValue="emit('select', date)"
        touchUI
      />
      <InputGroupAddon v-if="trailing" class="min-w-fit">{{ trailing }}</InputGroupAddon>
    </InputGroup>
  </div>
</template>
