import type { Client } from './schema';
import axiosApi from '../api';

const api = axiosApi();

export async function getAllClients(): Promise<Client[]> {
  const { data } = await api.get(`/clients`);
  return data;
}
