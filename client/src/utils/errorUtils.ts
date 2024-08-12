import { DEFAULT_SERVER_ERROR } from '@/consts';
import type { ToastServiceMethods } from 'primevue/toastservice';
import { ApiError } from './types';

export const showError = (
  toast: ToastServiceMethods,
  message: string,
  summary: string = 'Error'
) => {
  toast.add({
    severity: 'error',
    summary: summary,
    detail: message ?? DEFAULT_SERVER_ERROR,
    life: 20000,
  });
};

export const showSuccess = (
  toast: ToastServiceMethods,
  message: string,
  summary: string = 'Success'
) => {
  toast.add({ severity: 'success', summary: summary, detail: message, life: 20000 });
};

export const handleError = (toast: ToastServiceMethods, err: any) => {
  if (err instanceof ApiError) showError(toast, err.message, err.title);
  else if (err instanceof Error) showError(toast, err.message);
  else showError(toast, DEFAULT_SERVER_ERROR);
};
