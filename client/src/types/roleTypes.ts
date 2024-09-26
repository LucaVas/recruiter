export type Role = {
  id: number;
  name: RoleName;
  description: string;
  createdAt: Date;
  updatedAt: Date;
};
export type RoleName = 'RECRUITER' | 'ADMIN' | 'TESTER';
