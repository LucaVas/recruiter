<script setup lang="ts">
import DataTable from 'primevue/datatable';
import ApproveModal from './ApproveModal.vue';
import { onMounted, ref } from 'vue';
import Column from 'primevue/column';
import { filters, initFilters, clearFilter } from './filters';
import type { User, UserApprovalRequest } from '@/stores/user/schema';
import { ApiError } from '@/utils/types';
import { getAllUsers, approveUser } from '@/stores/user';
import { useToast } from 'primevue/usetoast';
import { columns } from '../candidates';
import UserCard from '@/components/users/UserCard.vue';
import UsersHeader from '@/components/users/UsersHeader.vue';
import UsersTableButtons from './UsersTableButtons.vue';
import { showError, showSuccess } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';

const toast = useToast();
const usersTableError = ref('');
const loading = ref(false);
const approvingUser = ref(false);
const users = ref<User[]>();

const approveModalOpen = ref(false);

const approve = async (request: UserApprovalRequest) => {
  approvingUser.value = true;
  try {
    await approveUser(request);
    approveModalOpen.value = false;
    initFilters();
    showSuccess(toast, 'User status updated successfully!');
    await initTable();
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    approvingUser.value = false;
  }
};

const initTable = async () => {
  loading.value = true;
  try {
    users.value = await getAllUsers();
  } catch (err) {
    if (err instanceof ApiError) usersTableError.value = err.message;
  } finally {
    loading.value = false;
  }
};

initFilters();
onMounted(async () => {
  await initTable();
});
</script>

<template>
  <DataTable
    v-model:filters="filters"
    filterDisplay="menu"
    size="small"
    :globalFilterFields="columns"
    :value="users"
    stripedRows
    paginator
    :rows="10"
    :loading="loading"
    dataKey="id"
    :rowsPerPageOptions="[5, 10, 20, 50]"
    class="w-full"
    tableStyle="margin-top: 1rem; margin-bottom: 1rem; font-size: 0.875rem; line-height: 1.25rem;"
  >
    <template #header>
      <UsersHeader :filters="filters" @clearFilter="clearFilter()" />
    </template>
    <template #empty> No users found. </template>
    <template #loading> Loading users, please wait... </template>

    <Column class="w-32">
      <template #body="{ data }">
        <ApproveModal
          :visible="approveModalOpen"
          @close="approveModalOpen = false"
          @approve="
            (comment) =>
              approve({
                userId: Number(data.id),
                approved: !data.approved,
                comment,
              })
          "
        />
        <UsersTableButtons :user="data" @openModal="approveModalOpen = true" />
      </template>
    </Column>
    <Column
      field="approved"
      header="Approved"
      dataType="boolean"
      class="w-8 text-center"
      bodyClass="text-center"
    >
      <template #body="{ data }">
        <i
          class="pi"
          :class="{
            'pi-check-circle text-green-500 ': data.approved,
            'pi-times-circle text-red-500': !data.approved,
          }"
        ></i>
      </template>
    </Column>

    <Column header="User">
      <template #body="{ data }">
        <UserCard :user="data" />
      </template>
    </Column>
  </DataTable>
</template>
