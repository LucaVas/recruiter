<template>
  <div class="flex w-full flex-col gap-2">
    <label v-if="label">{{ label }}</label>
    <InputGroup>
      <InputGroupAddon v-if="prefixIcon" class="min-w-fit">
        <i :class="`pi ${prefixIcon}`" />
      </InputGroupAddon>
      <InputText
        :type="type"
        :min="`${min}`"
        :max="`${max}`"
        v-model="input"
        :required="required ?? false"
        @input="emits('input', input)"
      />
    </InputGroup>
    <InputGroupAddon class="min-w-fit">
      {{ suffixText }}
    </InputGroupAddon>
  </div>
</template>

<script setup lang="ts">
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputText from 'primevue/inputtext';
import { ref } from 'vue';

const { label, prefixIcon, type, required, min, max, suffixText } = defineProps<{
  label?: string;
  prefixIcon?: string;
  type: string;
  required?: boolean;
  min?: number;
  max?: number;
  suffixText?: string;
}>();

const emits = defineEmits<{
  (e: 'input', content: string): void;
}>();

const input = ref('');
</script>
