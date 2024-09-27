import { defineStore } from 'pinia';
import baseApi from '@/api/baseApi';
import type { AxiosResponse } from 'axios';
import type { Job } from '@/types/jobTypes';
import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from '@/consts';
import type { CustomPage } from '@/types/paginationTypes';

const api = baseApi();
const baseApiPath = '/jobs';

type JobStoreState = {
  jobs: Job[];
  totalJobs: number;
};
const useJobStore = defineStore('jobStore', {
  state: (): JobStoreState => ({
    jobs: [],
    totalJobs: 0,
  }),
  getters: {},
  actions: {
    async fetchJobs(pageNumber: number, pageSize: number): Promise<void> {
      const { data }: AxiosResponse<CustomPage<Job>> = await api.get(
        `${baseApiPath}?page=${pageNumber ?? DEFAULT_PAGE_NUMBER}&pageSize=${pageSize ?? DEFAULT_PAGE_SIZE}`
      );
      this.jobs = data.elements;
      this.totalJobs = data.totalElements;
    },
  },
});

export default useJobStore;
