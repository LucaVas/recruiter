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
        privileges: ['RECRUITER', 'ADMIN'],
      },
      {
        icon: 'pi pi-plus',
        name: 'New job',
        shortcut: '⌘+N',
        view: 'NewJob',
        privileges: ['ADMIN'],
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
        privileges: ['RECRUITER'],
      },
      {
        icon: 'pi pi-list',
        name: 'All Candidacies',
        shortcut: '⌘+J',
        view: 'CandidaciesPage',
        privileges: ['ADMIN'],
      },
      {
        icon: 'pi pi-users',
        name: 'My Candidates',
        shortcut: '⌘+J',
        view: 'CandidatesPage',
        privileges: ['RECRUITER'],
      },
      {
        icon: 'pi pi-users',
        name: 'All Candidates',
        shortcut: '⌘+J',
        view: 'CandidatesPage',
        privileges: ['ADMIN'],
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
        privileges: ['ADMIN'],
      },
    ],
  },
  {
    group: 'Profile',
    links: [
      {
        name: 'Settings',
        icon: 'pi pi-cog',
        shortcut: '⌘+S',
        view: 'Settings',
        privileges: ['RECRUITER', 'ADMIN'],
      },
    ],
  },
]);
