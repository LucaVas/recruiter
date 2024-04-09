<script setup lang="ts">
import Button from 'primevue/button';
import { useRouter } from 'vue-router';

const router = useRouter();
const { data } = defineProps<{
  data: any;
}>();
</script>

<template>
  <div class="min-w-fit">
    <Button
      label="View"
      size="small"
      class="h-10 min-w-fit"
      outlined
      @click="router.push({ name: 'Job', params: { id: data.id } })"
    />
  </div>
</template>
