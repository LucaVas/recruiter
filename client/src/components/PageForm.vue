<script lang="ts" setup>
defineProps<{
  heading: string;
  formLabel: string;
}>();

defineEmits<{
  submit: [];
}>();
</script>

<template>
  <div class="flex h-full w-full flex-col items-center justify-center px-6 py-10 lg:px-8">
    <div class="sm:mx-auto sm:w-full sm:max-w-sm">
      <h1 class="text-2xl font-medium">{{ heading }}</h1>

      <div class="mt-6">
        <form class="space-y-4" :aria-label="formLabel" @submit.prevent="$emit('submit')">
          <slot />
        </form>

        <slot name="footer" />
      </div>
    </div>
  </div>
</template>
