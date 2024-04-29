import type { AuthUserInfoDto } from '@/stores/auth/schema';
import type { RoleName } from '@/stores/user/schema';

const TOKEN_KEY = 'token';

export function getAuthUserFromToken(token: string): AuthUserInfoDto {
  return JSON.parse(atob(token.split('.')[1])).user;
}

export function getAuthUserIdFromToken(token: string) {
  return getAuthUserFromToken(token).id;
}

export function getAuthUsernameFromToken(token: string): string {
  return getAuthUserFromToken(token).username;
}

export function getAuthRoleFromToken(token: string): RoleName {
  const role = getAuthUserFromToken(token).roleName;
  if (role !== 'ROLE_ADMIN' && role !== 'ROLE_RECRUITER') {
    throw new Error('Invalid role found in token');
  }
  return role;
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
