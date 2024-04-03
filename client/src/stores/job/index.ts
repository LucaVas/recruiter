import axios from 'axios';
import { defineStore } from 'pinia';
import type { JobDto, JobResponse, NewJob } from './types';
import { getStoredAccessToken } from '@/utils/auth';
import { ApiError } from '@/utils/types';

const axiosApi = axios.create({
  baseURL: `http://localhost:8080/api/v1`,
  timeout: 1000,
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${getStoredAccessToken(localStorage)}`,
  },
});

export const useJobStore = defineStore('jobStore', () => {
  async function getAllJobs(): Promise<JobDto[]> {
    try {
      const { data } = await axiosApi.get(`/jobs`);
      return data;
    } catch (err) {
      if (axios.isAxiosError(err)) {
        throw new ApiError(err.response?.data.message);
      } else {
        throw new ApiError('An unexpected error occurred');
      }
    }
  }
  async function addJob(newJob: NewJob): Promise<JobResponse> {
    try {
      const { data } = await axiosApi.post(`/jobs`, newJob);
      return data;
    } catch (err) {
      if (axios.isAxiosError(err)) {
        throw new ApiError(err.response?.data.message);
      } else {
        throw new ApiError('An unexpected error occurred');
      }
    }
  }
  async function deleteJob(id: number): Promise<void> {
    try {
      await axiosApi.delete(`/jobs/${id}`);
    } catch (err) {
      if (axios.isAxiosError(err)) {
        if (err.response?.status === 401)
          throw new ApiError("You don't have the authorities to perform this action.");
        throw new ApiError(err.response?.data.message);
      } else {
        throw new ApiError('An unexpected error occurred');
      }
    }
  }

  return { getAllJobs, addJob, deleteJob };
});
