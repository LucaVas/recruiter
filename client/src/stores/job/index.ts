import axios from 'axios';
import { defineStore } from 'pinia';
import type { Job, JobResponse, NewJob } from './types';
import { getStoredAccessToken } from '@/utils/auth';

const axiosApi = axios.create({
  baseURL: `http://localhost:8080/api/v1`,
  timeout: 1000,
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${getStoredAccessToken(localStorage)}`,
  },
});

export const useJobStore = defineStore('jobStore', () => {
  async function getAllJobs(): Promise<Job[]> {
    const res = await axiosApi.get(`/jobs`);
    return res.data;
  }
  async function addJob(newJob: NewJob): Promise<JobResponse> {
    const res = await axiosApi.post(`/jobs`, newJob);
    console.log(res.data)
    return res.data;
  }
  return { getAllJobs, addJob };
});
