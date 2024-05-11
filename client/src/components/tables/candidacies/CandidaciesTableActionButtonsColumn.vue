<template>
  <div class="min-w-fit">
    <Button
      label="Options"
      class="p-panel-header-icon p-link mr-2"
      @click="toggle"
      size="small"
      rounded
    />
    <Menu ref="menu" id="config_menu" :model="isAdmin ? adminItems : recruiterItems" popup />
  </div>
</template>

<script setup lang="ts">
import Button from 'primevue/button';
import { useRouter } from 'vue-router';
import { isAdmin } from '@/stores/auth';
import { ref } from 'vue';

const router = useRouter();
const { data } = defineProps<{
  data: any;
}>();
const emits = defineEmits<{
  (e: 'seeComments'): void;
}>();

const menu = ref();

const toggle = (event: Event) => {
  if (menu.value) menu.value.toggle(event);
};

const adminItems = ref([
  {
    label: 'Edit',
    icon: 'pi pi-pencil',
    command: () => {
      router.push({
        name: 'UpdateCandidacy',
        params: { jobId: data.job.id, pan: data.candidate.pan },
      });
    },
  },
  {
    separator: true,
  },
  {
    label: 'Delete',
    icon: 'pi pi-times',
  },
]);

const recruiterItems = ref([
  {
    label: 'Comments',
    icon: 'pi pi-envelope',
    command: () => {
      emits('seeComments');
    },
  },
]);
</script>
