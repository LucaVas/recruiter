import type { Client } from '@/stores/client/schema';
import type { Job } from '@/stores/job/schema';
import type { Skill } from '@/stores/skill/schema';
import { ref } from 'vue';

export const clients = ref<Client[]>([]);
export const skills = ref<Skill[]>([]);
export const loading = ref(false);
export const updatingJob = ref(false);
export const changingStatus = ref(false);
export const deletingJob = ref(false);
export const jobUpdated = ref(false);
export const jobDetails = ref<Job>();
