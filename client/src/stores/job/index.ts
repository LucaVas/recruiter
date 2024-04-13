import type { Job, JobResponse, JobStatus, NewJobRequest } from './types';
import axiosApi from '../api';

const api = axiosApi();

export async function getAllJobs(): Promise<Job[]> {
  const { data } = await api.get(`/jobs`);
  return data;
}
export async function createJob(newJob: NewJobRequest): Promise<JobResponse> {
  const { data } = await api.post(`/jobs`, newJob);
  return data;
}
export async function updateJob(updatedJob: Job): Promise<JobResponse> {
  const { data } = await api.put(`/jobs/${updatedJob.id}`, updatedJob);
  console.log(data);
  return data;
}
export async function changeJobStatus(jobId: number, newStatus: JobStatus): Promise<void> {
  await api.post(`/jobs/status/${jobId}`, { status: newStatus });
}
export async function deleteJob(id: number): Promise<void> {
  await api.delete(`/jobs/${id}`);
}
export async function getJob(id: number): Promise<Job> {
  const { data } = await api.get(`/jobs/${id}`);
  return data;
}
