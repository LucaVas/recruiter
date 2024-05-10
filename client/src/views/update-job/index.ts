import type { Client } from '@/stores/client/schema';
import type { Job } from '@/stores/job/schema';
import { ref } from 'vue';

export const clients = ref<Client[]>([]);
export const loading = ref(false);
export const updatingJob = ref(false);
export const jobUpdated = ref(false);
export const jobDetails = ref<Job>();