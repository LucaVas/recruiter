<template>
  <div class="min-w-fit">
    <Button
      label="Options"
      class="p-panel-header-icon p-link mr-2"
      @click="toggle"
      size="small"
      rounded
    />
    <Menu
      ref="menu"
      id="config_menu"
      :model="
        authStore.isAdmin
          ? adminItems.filter((item) => item.visible)
          : recruiterItems.filter((item) => item.visible)
      "
      popup
    />
  </div>
</template>

<script setup lang="ts">
import Button from 'primevue/button';
import { useRouter } from 'vue-router';
import { ref } from 'vue';
import useAuthStore from '@/stores/authStore';

const authStore = useAuthStore();
const router = useRouter();
const props = defineProps<{
  data: any;
}>();
const emits = defineEmits<{
  (e: 'seeComments'): void;
  (e: 'delete'): void;
  (e: 'seeFiles'): void;
  (e: 'withdraw'): void;
  (e: 'accept'): void;
  (e: 'reject'): void;
}>();

const menu = ref();

const toggle = (event: Event) => {
  if (menu.value) menu.value.toggle(event);
};

const adminItems = ref([
  {
    label: 'View',
    icon: 'pi pi-eye',
    visible: true,
    command: () => {
      router.push({
        name: 'Candidacy',
        params: { id: props.data.id },
      });
    },
  },
  {
    separator: true,
  },
  {
    label: 'Accept',
    icon: 'pi pi-check-square',
    visible: props.data.status === 'SENT_TO_CLIENT',
    command: () => {
      emits('accept');
    },
  },
  {
    label: 'Reject',
    icon: 'pi pi-ban',
    visible: props.data.status === 'ACCEPTED' || props.data.status === 'SENT_TO_CLIENT',
    command: () => {
      emits('reject');
    },
  },
  {
    separator: true,
  },
  {
    label: 'Comments',
    icon: 'pi pi-envelope',
    visible: true,
    command: () => {
      emits('seeComments');
    },
  },
  {
    label: 'Files',
    icon: 'pi pi-folder-open',
    visible: true,
    command: () => {
      emits('seeFiles');
    },
  },
  {
    separator: true,
  },
  {
    label: 'Delete',
    icon: 'pi pi-times',
    visible: true,
    command: () => {
      emits('delete');
    },
  },
]);

const recruiterItems = ref([
  {
    label: 'Edit',
    icon: 'pi pi-pencil',
    visible: true,
    command: () => {
      router.push({
        name: 'UpdateCandidacy',
        params: { id: props.data.id },
      });
    },
  },
  {
    separator: true,
  },
  {
    label: 'Comments',
    icon: 'pi pi-envelope',
    visible: true,
    command: () => {
      emits('seeComments');
    },
  },
  {
    label: 'Files',
    icon: 'pi pi-folder-open',
    visible: true,
    command: () => {
      emits('seeFiles');
    },
  },
  {
    separator: true,
  },
  {
    label: 'Withdraw',
    icon: 'pi pi-times',
    visible: props.data.status !== 'WITHDRAWN',
    command: () => {
      emits('withdraw');
    },
  },
]);
</script>
