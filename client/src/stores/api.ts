import { getStoredAccessToken } from '@/utils/auth';
import { ApiError } from '@/utils/types';
import axios, { type RawAxiosRequestHeaders } from 'axios';
import { logout } from './user';

export default () => {
  let headers: RawAxiosRequestHeaders = {
    'Content-Type': 'application/json',
  };

  const accessToken = getStoredAccessToken(localStorage);

  if (accessToken && accessToken !== '')
    headers = { ...headers, Authorization: `Bearer ${accessToken}` };

  const instance = axios.create({
    baseURL: `http://localhost:8080/api/v1`,
    timeout: 1000,
    headers: headers,
  });

  instance.interceptors.request.use(
    (request) => {
      return request;
    },
    (error) => {
      if (axios.isAxiosError(error)) {
        throw new ApiError(error.response?.data.message);
      }
      return Promise.reject(error);
    }
  );

  instance.interceptors.response.use(
    (response) => {
      if (response.status === 401) {
        alert('You are not authorized');
      }
      return response;
    },
    (error) => {
      if (axios.isAxiosError(error)) {
        // if (error.response?.status === 401) {
        //   console.log('Error');
        //   // logout();
        //   // window.location.href = '/login';
        // }
        throw new ApiError(error.response?.data.message);
      } else if (error.response && error.response.data) {
        throw new ApiError(error.response.data);
      } else {
        throw new ApiError('An unexpected error occurred');
      }
    }
  );

  return instance;
};
