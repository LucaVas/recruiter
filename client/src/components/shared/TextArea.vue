<script setup lang="ts">
import { ref } from 'vue';
import Textarea from 'primevue/textarea';

const { label, placeholder, model } = defineProps<{
  label?: string;
  placeholder?: string;
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
    <Textarea
      v-model="text"
      class="w-full"
      rows="4"
      cols="30"
      :placeholder="placeholder"
      style="resize: none"
      @input="emit('input', text)"
    />
  </div>
</template>
