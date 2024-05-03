import { ref } from 'vue';
import type { MenuItem } from '../types';

export const roleTag = ref('');
export const usernameTag = ref('');
export const roleMenuItems = ref<MenuItem[]>();

export const menuItems = ref<MenuItem[]>([
  {
    group: 'Jobs',
    links: [
      {
        icon: 'pi pi-list',
        name: 'All jobs',
        shortcut: '⌘+J',
        view: 'Dashboard',
        privileges: ['ROLE_RECRUITER', 'ROLE_ADMIN'],
      },
      {
        icon: 'pi pi-plus',
        name: 'New job',
        shortcut: '⌘+N',
        view: 'NewJob',
        privileges: ['ROLE_ADMIN'],
      },
    ],
  },
  {
    group: 'Candidacies',
    links: [
      {
        icon: 'pi pi-list',
        name: 'My Candidacies',
        shortcut: '⌘+J',
        view: 'CandidaciesPage',
        privileges: ['ROLE_RECRUITER', 'ROLE_ADMIN'],
      },
      {
        icon: 'pi pi-users',
        name: 'My Candidates',
        shortcut: '⌘+J',
        view: 'CandidatesPage',
        privileges: ['ROLE_RECRUITER', 'ROLE_ADMIN'],
      },
    ],
  },
  {
    group: 'Users',
    links: [
      {
        icon: 'pi pi-users',
        name: 'All users',
        shortcut: '⌘+U',
        view: 'UsersView',
        privileges: ['ROLE_ADMIN'],
      },
    ],
  },
  {
    group: 'Profile',
    links: [
      {
        name: 'My performance',
        icon: 'pi pi-chart-line',
        shortcut: '⌘+P',
        view: 'Dashboard',
        privileges: ['ROLE_RECRUITER', 'ROLE_ADMIN'],
      },
      {
        name: 'Settings',
        icon: 'pi pi-cog',
        shortcut: '⌘+S',
        view: 'Settings',
        privileges: ['ROLE_RECRUITER', 'ROLE_ADMIN'],
      },
    ],
  },
]);
