import axiosApi from '../api';
import type { Job, JobStatus, NewJob } from './schema';

const api = axiosApi();

export class JobApi {
  async getJob(id: number): Promise<Job> {
    const { data } = await api.get(`/jobs/${id}`);
    return data;
  }
  async getAllJobs(): Promise<Job[]> {
    const { data } = await api.get(`/jobs`);
    return data;
  }
  async addJob(newJob: NewJob): Promise<Job> {
    const { data } = await api.post(`/jobs`, newJob);
    return data;
  }
  async updateJob(id: number, job: Job): Promise<Job> {
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
