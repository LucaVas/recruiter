import { DEFAULT_SERVER_ERROR } from '@/consts';
import type { ToastServiceMethods } from 'primevue/toastservice';

export const showError = (
  toast: ToastServiceMethods,
  message: string,
  summary: string = 'Error'
) => {
  toast.add({
    severity: 'error',
    summary: summary,
    detail: message ?? DEFAULT_SERVER_ERROR,
    life: 3000,
  });
};

export const showSuccess = (
  toast: ToastServiceMethods,
  message: string,
  summary: string = 'Success'
) => {
  toast.add({ severity: 'success', summary: summary, detail: message, life: 3000 });
};
