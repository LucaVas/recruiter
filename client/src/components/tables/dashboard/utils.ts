import { FilterMatchMode, FilterOperator } from 'primevue/api';
import { ref } from 'vue';
import type { Skill } from '@/stores/skill/types';

export const filters = ref();
export const globalFiltersFields = ref([
  'id',
  'client',
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
    id: { value: null, matchMode: FilterMatchMode.EQUALS },
    client: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.STARTS_WITH }],
    },
    name: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.STARTS_WITH }],
    },
    contractType: { value: null, matchMode: FilterMatchMode.IN },
    experienceRange: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.STARTS_WITH }],
    },
    salaryBudget: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.GREATER_THAN_OR_EQUAL_TO }],
    },
    noticePeriodInDays: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.GREATER_THAN_OR_EQUAL_TO }],
    },
    numberOfCandidates: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.GREATER_THAN_OR_EQUAL_TO }],
    },
    creationDate: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.DATE_IS }],
    },
    status: { value: null, matchMode: FilterMatchMode.IN },
  };
};

export const clearFilter = () => {
  initFilters();
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
