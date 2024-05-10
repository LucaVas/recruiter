import { apiBase } from '@/config';
import { getStoredAccessToken } from '@/utils/auth';
import { ApiError } from '@/utils/types';
import axios, { type RawAxiosRequestHeaders } from 'axios';

export default () => {
  let headers: RawAxiosRequestHeaders = {
    'Content-Type': 'application/json',
  };

  const accessToken = getStoredAccessToken(localStorage);

  if (accessToken && accessToken !== '')
    headers = { ...headers, Authorization: `Bearer ${accessToken}` };

  const instance = axios.create({
    baseURL: apiBase,
    timeout: 10000,
    headers: headers,
  });

  instance.interceptors.request.use(
    (request) => {
      // Check if the request is a multipart/form-data request
      if (
        typeof request.headers['Content-Type'] === 'string' &&
        request.headers['Content-Type'].startsWith('multipart/form-data')
      ) {
        // Set Content-Type to multipart/form-data for multipart requests
        request.headers['Content-Type'] = 'multipart/form-data';
      }
      return request;
    },
    (error) => {
      console.log('Error in API request', error);
      if (axios.isAxiosError(error)) {
        const message = error.response?.data.message;
        const statusCode = error.response?.data.statusCode;
        throw new ApiError(message, statusCode);
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
      console.log('Error in API response', error);
      if (axios.isAxiosError(error)) {
        const message = error.response?.data.message;
        const statusCode = error.response?.status;
        throw new ApiError(message, statusCode);
      } else if (error.response && error.response.data) {
        throw new ApiError(error.response.data, error.response.statusCode);
      } else {
        throw new ApiError('An unexpected error occurred', 500);
      }
    }
  );

  return instance;
};
