import { FilterMatchMode } from 'primevue/api';
import { ref } from 'vue';
import type { Skill } from '@/types/skillTypes';

export const filters = ref();
export const globalFiltersFields = ref([
  'client.name',
  'name',
  'experienceRange',
  'contractType',
  'salaryBudget',
  'noticePeriodInDays',
  'numberOfCandidates',
  'creationDate',
  'status',
]);

export const initFilters = () => {
  filters.value = {
    global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  };
};

export const getSeverity = (status: string) => {
  switch (status.toLowerCase()) {
    case 'closed':
      return 'danger';

    case 'open':
      return 'success';

    case 'pending':
      return 'warning';

    default:
      return 'info';
  }
};

export const getContractType = (type: string) => {
  switch (type.toLowerCase()) {
    case 'PERMANENT':
      return 'info';

    case 'TEMPORARY':
      return 'pending';

    default:
      return 'info';
  }
};

export const getClientIcon = (clientName: string) => {
  switch (clientName.toLowerCase()) {
    case 'infosys':
      return 'infosys.svg';

    case 'ibm':
      return 'ibm.svg';

    case 'accenture':
      return 'accenture.svg';

    default:
      return 'placeholder.png';
  }
};

export const getSkills = (skills: Skill[]): string => {
  return skills.map((skill) => skill.name).join(', ');
};
