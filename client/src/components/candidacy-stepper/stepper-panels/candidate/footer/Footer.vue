<script setup lang="ts">
import Button from 'primevue/button';

const emits = defineEmits<{
  (e: 'back'): void;
  (e: 'next'): void;
}>();
</script>

<template>
  <div class="flex justify-end pt-4">
    <Button
      label="Next"
      icon="pi pi-arrow-right"
      iconPos="right"
      @click="emits('next')"
    />
  </div>
</template>
