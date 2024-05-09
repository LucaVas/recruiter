type Industry =
  | 'IT'
  | 'FINANCE'
  | 'HEALTHCARE'
  | 'EDUCATION'
  | 'MARKETING'
  | 'SALES'
  | 'HUMAN_RESOURCES'
  | 'ENGINEERING'
  | 'OTHER';

export const industries = [
  { name: 'IT', value: 'IT' },
  { name: 'Finance', value: 'FINANCE' },
  { name: 'Healthcare', value: 'HEALTHCARE' },
  { name: 'Education', value: 'EDUCATION' },
  { name: 'Marketing', value: 'MARKETING' },
  { name: 'Sales', value: 'SALES' },
  { name: 'Human Resources', value: 'HUMAN_RESOURCES' },
  { name: 'Engineering', value: 'ENGINEERING' },
  { name: 'Other', value: 'OTHER' },
];

export type Client = {
  id: number;
  name: string;
  industry: Industry;
  createdDTime: Date;
  modifiedDTime: Date;
};

export type NewClient = {
  name: string;
  industry: Industry;
};
