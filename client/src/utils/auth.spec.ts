import { getAuthUserFromToken } from './auth';

describe('getUserFromToken', () => {
  it('should return the user id from a valid JWT token', () => {
    const token =
      'eyJhbGciOiJIUzUxMiJ9.eyJpZCI6MTIzNDU2Nzg5LCJ1c2VyIjp7ImlkIjoxMjM0NTY3ODksInVzZXJuYW1lIjoicmVjcnVpdGVyIiwicm9sZU5hbWUiOiJST0xFX1JFQ1JVSVRFUiJ9fQ.jLmFUopJEItKBwT-K-S91yBDPVjo39UZeVd6aONT-UMOOZ7dhIQMhTMqbve_m9ChWxvghVDKHR15zmZ6FdFeDg';

    const authUser = getAuthUserFromToken(token);
    expect(authUser).toEqual({
      id: 123456789,
      username: 'recruiter',
      roleName: 'ROLE_RECRUITER',
    });
  });

  it('should throw an error for an invalid token', () => {
    const token = 'invalid-token';
    expect(() => getAuthUserFromToken(token)).toThrow();
  });
});
