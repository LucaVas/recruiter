<template>
  <div>
    <Divider />
    <FileUpload
      name="demo[]"
      url="/api/upload"
      @upload="onAdvancedUpload()"
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

<script setup lang="ts">
import { useToast } from 'primevue/usetoast';
import FileUpload from 'primevue/fileupload';
const toast = useToast();

const onAdvancedUpload = () => {
  toast.add({ severity: 'info', summary: 'Success', detail: 'File Uploaded', life: 3000 });
};
</script>
