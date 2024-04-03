<script setup lang="ts">
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import TriStateCheckbox from 'primevue/tristatecheckbox';
import ApproveModal from './ApproveModal.vue';
import { onMounted, ref } from 'vue';
import Column from 'primevue/column';
import { filters, initFilters, clearFilter } from './filters';
import type { UserDto } from '../../stores/user/types';
import { ApiError } from '../../utils/types';
import { formatDate } from './utils';
import { getAllUsers, approveUser } from '@/stores/user';

const usersTableError = ref('');
const loading = ref(false);
const approvingUser = ref(false);
const users = ref<UserDto[]>();
const columns = ref([
  'id',
  'username',
  'email',
  'mobile',
  'city',
  'country',
  'roles',
  'comments',
  'approved',
  'approver',
  'approvedOn',
]);
const approvalRequest = ref({
  userId: '',
  approved: false,
  comments: '',
});
const approveModalOpen = ref(false);

async function approve() {
  approvingUser.value = true;
  try {
    await approveUser({
      ...approvalRequest.value,
      userId: Number(approvalRequest.value.userId),
    });
  } catch (err) {
    if (err instanceof ApiError) usersTableError.value = err.message;
  } finally {
    initFilters();
    approvingUser.value = false;
  }
  approveModalOpen.value = false;
  await initTable();
}

async function initTable() {
  loading.value = true;
  try {
    users.value = await getAllUsers();
  } catch (err) {
    if (err instanceof ApiError) usersTableError.value = err.message;
  } finally {
    loading.value = false;
  }
}
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
      <UsersTableHeader @clearFilter="clearFilter()" />
    </template>
    <template #empty> No users found. </template>
    <template #loading> Loading users, please wait... </template>

    <Column field="id" header="ID" dataType="numeric" class="min-w-16">
      <template #body="{ data }">
        {{ data.id }}
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter"
          placeholder="Search by user id"
        />
      </template>
    </Column>
    <Column field="username" header="Username" class="min-w-52">
      <template #body="{ data }">
        <div class="flex items-center gap-3">
          {{ data.username }}
        </div>
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by username"
        />
      </template>
    </Column>
    <Column field="email" header="Email" class="min-w-52">
      <template #body="{ data }">
        {{ data.email }}
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter"
          placeholder="Search by email"
        />
      </template>
    </Column>
    <Column field="mobile" header="Mobile" class="min-w-52">
      <template #body="{ data }"> {{ data.mobile }} </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by mobile"
        />
      </template>
    </Column>
    <Column field="city" header="City" class="min-w-52">
      <template #body="{ data }"> {{ data.city }} </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by city"
        />
      </template>
    </Column>
    <Column field="country" header="Country" class="min-w-52">
      <template #body="{ data }"> {{ data.country }} </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter min-w-52"
          placeholder="Search by country"
        />
      </template>
    </Column>
    <Column field="comments" header="Comments" class="max-w-52 truncate text-nowrap">
      <template #body="{ data }"> {{ data.comments }} </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="text"
          class="p-column-filter"
          placeholder="Search by country"
        />
      </template>
    </Column>
    <Column field="approvedOn" header="Approved On" dataType="date" class="min-w-40">
      <template #body="{ data }">
        {{ formatDate(data.approvedOn) }}
      </template>
      <template #filter="{ filterModel }">
        <InputText
          v-model="filterModel.value"
          type="date"
          class="p-column-filter"
          placeholder="Search by approved date"
        />
      </template>
    </Column>
    <Column
      field="approved"
      header="Approved"
      dataType="boolean"
      class="text-center"
      bodyClass="text-center"
      style="min-width: 8rem"
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
      <template #filter="{ filterModel }">
        <label for="verified-filter" class="font-bold"> Approved </label>
        <TriStateCheckbox v-model="filterModel.value" inputId="verified-filter" />
      </template>
    </Column>

    <Column field="action" header="" class="min-w-10">
      <template #body="{ data }">
        <ApproveModal
          :visible="approveModalOpen"
          @closeModal="approveModalOpen = false"
          @approve="
            (comments) => {
              approvalRequest.comments = comments;
              approvalRequest.approved = !data.approved;
              approvalRequest.userId = data.id;
              approve();
            }
          "
        />
        <div class="flex gap-2">
          <Button
            v-if="!data.approved"
            icon="pi pi-check"
            severity="success"
            class="h-8 w-8"
            rounded
            @click="approveModalOpen = true"
          />
          <Button
            v-else
            icon="pi pi-times"
            severity="danger"
            rounded
            class="h-8 w-8"
            @click="approveModalOpen = true"
          />
        </div>
      </template>
    </Column>
  </DataTable>
</template>
