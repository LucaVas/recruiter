import type { JobDto, JobResponse, NewJob } from './types';
import axiosApi from '../api';

const api = axiosApi();

export async function getAllJobs(): Promise<JobDto[]> {
  const { data } = await api.get(`/jobs`);
  return data;
}
export async function addJob(newJob: NewJob): Promise<JobResponse> {
  const { data } = await api.post(`/jobs`, newJob);
  return data;
}
export async function updateJob(updatedJob: JobDto): Promise<JobResponse> {
  const { data } = await api.put(`/jobs/${updatedJob.id}`, updatedJob);
  console.log(data);
  return data
}
export async function deleteJob(id: number): Promise<void> {
  await api.delete(`/jobs/${id}`);
}
export async function getJobDetails(id: number): Promise<JobDto> {
  const { data } = await api.get(`/jobs/${id}`);
  return data;
}
