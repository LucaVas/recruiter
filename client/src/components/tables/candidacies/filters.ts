import { FilterMatchMode, FilterOperator } from 'primevue/api';
import { ref } from 'vue';

export const filters = ref();

export const clearFilter = () => {
  initFilters();
};

export const initFilters = () => {
  filters.value = {
    global: { value: null, matchMode: FilterMatchMode.CONTAINS },
    candidate: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    job: {
      operator: FilterOperator.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    status: { value: null, matchMode: FilterMatchMode.IN },
  };
};
