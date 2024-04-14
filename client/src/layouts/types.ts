import type { RoleName } from '@/stores/user/types';

export type MenuItem = {
  group: string;
  links: {
    icon: string;
    name: string;
    shortcut: string;
    view: string;
    privileges: RoleName[];
    command?: (...args: any) => any;
  }[];
};
