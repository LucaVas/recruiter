import type { AuthUser } from '@/stores/user/types';

const TOKEN_KEY = 'token';

export function getUsernameFromToken(token: string): AuthUser {
  return JSON.parse(atob(token.split('.')[1])).sub;
}

export function getIdFromToken(token: string): AuthUser {
  return JSON.parse(atob(token.split('.')[1])).jti;
}

// token
export function clearStoredAccessToken(storage: Storage) {
  storage.removeItem(TOKEN_KEY);
}

export function storeAccessToken(storage: Storage, token: string) {
  storage.setItem(TOKEN_KEY, token);
}

export function getStoredAccessToken(storage: Storage): string | null {
  return storage.getItem(TOKEN_KEY);
}
