<template>
  <Dialog
    :visible="visible"
    :style="{ width: '25rem' }"
    position="top"
    :closable="false"
    :modal="true"
    :draggable="false"
  >
    <div class="mb-6 flex w-full justify-center">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 30 30" x="0px" y="0px" class="w-20">
        <g>
          <path
            d="M16,30A14,14,0,1,1,27.382,7.847a1,1,0,0,1-.105,1.29l-.569.57-.8-.8-.149.107A12,12,0,1,0,28,16h2A14.015,14.015,0,0,1,16,30Z"
          />
          <path
            d="M14,22a1,1,0,0,1-.707-.293l-6-6,1.414-1.414L14,19.586,25.293,8.293l1.414,1.414-12,12A1,1,0,0,1,14,22Z"
          />
          <rect width="30" height="30" fill="none" />
        </g>
      </svg>
    </div>
    <div class="space-y-6">
      <h2 class="text-center text-xl">{{ title }}</h2>
      <h4 class="p-text-secondary block text-center">{{ message }}</h4>
    </div>
    <Button
      class="mt-8 w-full"
      type="button"
      severity="success"
      label="Done"
      @click="$emit('close')"
    ></Button>
  </Dialog>
</template>

<script setup lang="ts">
const { visible, title, message } = defineProps<{
  visible: boolean;
  title: string;
  message: string;
}>();
defineEmits<{
  (e: 'close'): void;
}>();
</script>
