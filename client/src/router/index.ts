import { createRouter, createWebHistory } from 'vue-router';
import { authenticate, hideForAdmin, showForAdmin } from './guards';
import DashboardLayout from '@/layouts/dashboard/DashboardLayout.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: DashboardLayout,
      beforeEnter: [authenticate],
      children: [
        {
          path: '/',
          name: 'Dashboard',
          component: () => import('@/views/Dashboard.vue'),
        },
        {
          path: '/jobs/new',
          beforeEnter: [showForAdmin],
          name: 'NewJob',
          component: () => import('@/views/new-job/NewJob.vue'),
        },
        {
          path: '/candidacies/job=:id',
          name: 'NewCandidacy',
          beforeEnter: [hideForAdmin],
          component: () => import('@/views/new-candidacy/NewCandidacy.vue'),
        },
        {
          path: '/candidacies/edit/job=:jobId&candidate=:pan',
          beforeEnter: [showForAdmin],
          name: 'UpdateCandidacy',
          component: () => import('@/views/update-candidacy/UpdateCandidacy.vue'),
        },
        {
          path: '/candidates',
          name: 'CandidatesPage',
          component: () => import('@/views/candidates-page/CandidatesPage.vue'),
        },
        {
          path: '/candidacies',
          name: 'CandidaciesPage',
          component: () => import('@/views/candidacies-page/CandidaciesPage.vue'),
        },
        {
          path: '/users',
          beforeEnter: [showForAdmin],
          name: 'UsersView',
          component: () => import('@/views/UsersView.vue'),
        },
        {
          path: '/jobs/:id',
          name: 'Job',
          component: () => import('@/views/job/Job.vue'),
        },
        {
          path: '/jobs/update/:id',
          beforeEnter: [showForAdmin],
          name: 'UpdateJob',
          component: () => import('@/views/update-job/UpdateJob.vue'),
        },
        {
          path: '/settings',
          name: 'Settings',
          component: () => import('@/views/settings/SettingsView.vue'),
        },
      ],
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/login/LoginView.vue'),
    },
    {
      path: '/signup',
      name: 'Signup',
      component: () => import('@/views/signup/SignupView.vue'),
    },
    {
      path: '/forgot-password',
      name: 'ForgotPassword',
      component: () => import('@/views/forgot-password/ForgotPasswordView.vue'),
    },
    {
      path: '/password-reset/token=:token',
      name: 'PasswordReset',
      component: () => import('@/views/password-reset/PasswordResetView.vue'),
    },
    {
      path: '/:catchAll(.*)',
      name: 'NotFound',
      component: () => import('@/views/not-found/NotFoundPage.vue'),
      meta: {
        requiresAuth: false,
      },
    },
  ],
});

export default router;
