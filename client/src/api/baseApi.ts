import { apiBase } from '@/config';
import { getStoredAccessToken } from '@/utils/auth';
import { ApiError } from '@/utils/types';
import axios, { type RawAxiosRequestHeaders } from 'axios';
import { logout } from './authApi';

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
      if (axios.isAxiosError(error)) {
        const title = error.response?.data.title;
        const message = error.response?.data.detail;
        const statusCode = error.response?.status;
        throw new ApiError(message, statusCode, title);
      }
      return Promise.reject(error);
    }
  );

  instance.interceptors.response.use(
    (response) => {
      return response;
    },
    (error) => {
      if (axios.isAxiosError(error)) {
        console.log(error);

        if (!error.response) throw new ApiError(error.message, Number(error.code), 'Error');

        let responseData;

        if (error.response?.config.responseType === 'arraybuffer') {
          const stringData = String.fromCharCode.apply(
            null,
            new Uint8Array(error.response?.data) as any
          );
          responseData = JSON.parse(stringData);
        } else {
          responseData = error.response?.data;
        }

        const title = responseData.title;
        const message = responseData.detail;
        const statusCode = Number(responseData.status);

        if (error.code === 'ERR_NETWORK') {
          logoutProcess();
          return;
        }

        if ((message && message.includes('JWT')) || message.includes('Access denied')) {
          logoutProcess();
          return;
        }

        throw new ApiError(message, statusCode, title);
      } else if (error.response && error.response.data) {
        throw new ApiError(error.response.data, error.response.statusCode);
      } else {
        throw new ApiError('An unexpected error occurred', 500);
      }
    }
  );

  return instance;
};

const logoutProcess = () => {
  logout();
  window.location.reload();
};
