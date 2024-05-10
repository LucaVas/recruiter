import type { ToastServiceMethods } from 'primevue/toastservice';

export const showError = (toast: ToastServiceMethods, message: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: message, life: 3000 });
};

export const showSuccess = (toast: ToastServiceMethods, message: string) => {
  toast.add({ severity: 'success', summary: 'Success', detail: message, life: 3000 });
};
