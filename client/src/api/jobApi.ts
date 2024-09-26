import type { AxiosResponse } from 'axios';
import axiosApi from './baseApi';
import type { CustomPage } from '../types/paginationTypes';
import { type Job, type JobResponse, type JobStatus, type NewJob } from '../types/jobTypes';
import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from '@/consts';

const api = axiosApi();
const baseApi = '/jobs';

export async function getAllJobs(pageNumber: number, pageSize: number): Promise<CustomPage<Job>> {
  const { data } = (await api.get(
    `${baseApi}?page=${pageNumber ?? DEFAULT_PAGE_NUMBER}&pageSize=${pageSize ?? DEFAULT_PAGE_SIZE}`
  )) as AxiosResponse<CustomPage<Job>>;
  return data;
}
export async function createJob(newJob: NewJob): Promise<JobResponse> {
  const { data } = await api.post(`${baseApi}`, newJob);
  return data;
}
export async function updateJob(updatedJob: Job): Promise<JobResponse> {
  const { data } = await api.put(`${baseApi}/${updatedJob.id}`, updatedJob);
  return data;
}
export async function changeJobStatus(jobId: number, newStatus: JobStatus): Promise<void> {
  await api.post(`${baseApi}/status/${jobId}`, { status: newStatus });
}
export async function deleteJob(id: number): Promise<void> {
  await api.delete(`${baseApi}/${id}`);
}
export async function getJob(id: number): Promise<Job> {
  const { data } = await api.get(`${baseApi}/${id}`);
  return data;
}
