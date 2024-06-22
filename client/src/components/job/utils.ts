import type { ContractType, JobStatus } from '@/stores/job/schema';
import { ref } from 'vue';

export const jobStatuses = ref<{ name: string; value: JobStatus }[]>([
  { name: 'Open', value: 'OPEN' },
  { name: 'Closed', value: 'CLOSED' },
  { name: 'No CV accepted', value: 'NO_CV_ACCEPTED' },
]);
export const contractTypes = ref<{ name: string; value: ContractType }[]>([
  { name: 'Permanent', value: 'PERMANENT' },
  { name: 'Temporary', value: 'TEMPORARY' },
]);

export const formatJobStatus = (status: JobStatus) => {
  switch (status) {
    case 'OPEN':
      return 'Open';
    case 'CLOSED':
      return 'Closed';
    case 'NO_CV_ACCEPTED':
      return 'No CV accepted';
    case 'ARCHIVED':
      return 'Archived';
  }
};

export const getJobSeverity = (status: JobStatus) => {
  switch (status) {
    case 'OPEN':
      return 'success';
    case 'NO_CV_ACCEPTED':
      return 'warning';
    case 'CLOSED':
      return 'danger';
    case 'ARCHIVED':
      return 'contrast';
  }
};

export const getJobStatusIcon = (status: JobStatus) => {
  switch (status) {
    case 'OPEN':
      return 'pi pi-lock-open';
    case 'NO_CV_ACCEPTED':
      return 'pi pi-lock';
    case 'CLOSED':
      return 'pi pi-times-circle';
    case 'ARCHIVED':
      return 'pi pi-folder';
  }
};
