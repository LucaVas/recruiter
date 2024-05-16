import type { MenuItem } from './types';

export function getMenuItems(isAdmin: boolean, items: MenuItem[]) {
  const filteredItems = items
    .map((group) => ({
      ...group,
      links: group.links.filter((link) =>
        link.privileges.includes(isAdmin ? 'ADMIN' : 'RECRUITER')
      ),
    }))
    .filter((group) => group.links.length > 0);
  return filteredItems;
}
