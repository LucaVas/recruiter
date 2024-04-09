<script setup lang="ts">
import { useToast } from 'primevue/usetoast';
import Toast from 'primevue/toast'
import FileUpload from 'primevue/fileupload'
const toast = useToast();

const onAdvancedUpload = () => {
  toast.add({ severity: 'info', summary: 'Success', detail: 'File Uploaded', life: 3000 });
};

</script>

<template>
  <div class="card">
    <Toast />
    <FileUpload
      name="demo[]"
      url="/api/upload"
      @upload="onAdvancedUpload($event)"
      :multiple="true"
      accept="image/*"
      :maxFileSize="1000000"
    >
      <template #empty>
        <p>Drag and drop files to here to upload.</p>
      </template>
    </FileUpload>
  </div>
</template>
