import { changeJobStatusRequestSchema, deleteJobSchema, newJobRequestSchema, updateJobRequestSchema, type Job, type JobResponse, type JobStatus, type NewJobRequest } from './schema';
import axiosApi from '../api';

const api = axiosApi();

export async function getAllJobs(): Promise<Job[]> {
  const { data } = await api.get(`/jobs`);
  return data;
}
export async function createJob(newJob: NewJobRequest): Promise<JobResponse> {
  newJobRequestSchema.parse(newJob);
  const { data } = await api.post(`/jobs`, newJob);
  console.log(data);
  return data;
}
export async function updateJob(updatedJob: Job): Promise<JobResponse> {
  updateJobRequestSchema.parse(updatedJob);
  const { data } = await api.put(`/jobs/${updatedJob.id}`, updatedJob);
  console.log(data);
  return data;
}
export async function changeJobStatus(jobId: number, newStatus: JobStatus): Promise<void> {
  changeJobStatusRequestSchema.parse({ status: newStatus });
  await api.post(`/jobs/status/${jobId}`, { status: newStatus });
}
export async function deleteJob(id: number): Promise<void> {
  deleteJobSchema.parse(id);
  await api.delete(`/jobs/${id}`);
}
export async function getJob(id: number): Promise<Job> {
  const { data } = await api.get(`/jobs/${id}`);
  return data;
}
