import axiosApi from './baseApi';
import { type Job, type JobResponse, type JobStatus, type NewJob } from '../types/jobTypes';

const api = axiosApi();
const baseApi = '/jobs';

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
