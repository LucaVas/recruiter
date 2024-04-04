import type { RoleName } from '@/stores/user/types';
import type { MenuItem } from './DashboardLayout.vue';

export function getRoleTag(roleName: RoleName): string {
  switch (roleName) {
    case 'ROLE_ADMIN':
      return 'Admin';
    case 'ROLE_RECRUITER':
      return 'Recruiter';
    default:
      return '';
  }
}

export function getMenuItems(roleName: RoleName, items: MenuItem[]) {
  const filteredItems = items
    .map((group) => ({
      ...group,
      links: group.links.filter((link) => link.privileges.includes(roleName)),
    }))
    .filter((group) => group.links.length > 0);
  return filteredItems;
}
