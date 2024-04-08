import type { ContractTypeDto, ContractTypeName, JobStatus } from '@/stores/job/types';
import { ref } from 'vue';

export const jobStatuses = ref<{ name: string; value: JobStatus }[]>([
  { name: 'Open', value: 'OPEN' },
  { name: 'Closed', value: 'CLOSED' },
  { name: 'No CV accepted', value: 'NO_CV_ACCEPTED' },
]);
export const contractTypes = ref<{ name: string; value: ContractTypeName }[]>([
  { name: 'Permanent', value: 'PERMANENT' },
  { name: 'Temporary', value: 'TEMPORARY' },
]);

export const mapToOption = (status: JobStatus): string => {
  switch (status) {
    case 'CLOSED':
      return 'Closed';
    case 'NO_CV_ACCEPTED':
      return 'No CV accepted';
    default:
      return 'Open';
  }
};
