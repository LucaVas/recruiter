import axiosApi from '../api';
import type { JobDto, JobStatus, NewJobRequest } from './store';

const api = axiosApi();

export class JobApi {
  async getJob(id: number): Promise<JobDto> {
    const { data } = await api.get(`/jobs/${id}`);
    return data;
  }
  async getAllJobs(): Promise<JobDto[]> {
    const { data } = await api.get(`/jobs`);
    return data;
  }
  async addJob(newJob: NewJobRequest): Promise<JobDto> {
    const { data } = await api.post(`/jobs`, newJob);
    return data;
  }
  async updateJob(id: number, job: JobDto): Promise<JobDto> {
    const { data } = await api.put(`/jobs/${id}`, job);
    return data;
  }
  async changeJobStatus(id: number, newStatus: JobStatus) {
    await api.post(`/jobs/status/${id}`, { status: newStatus });
  }
  async deleteJob(id: number) {
    await api.delete(`/jobs/${id}`);
  }
}
