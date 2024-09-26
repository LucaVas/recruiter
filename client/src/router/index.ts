import { createRouter, createWebHistory } from 'vue-router';
import { hideForAuthGuard, showForAdminGuard, showForAuthGuard } from './guards';
import DashboardLayout from '@/layouts/dashboard/DashboardLayout.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: DashboardLayout,
      beforeEnter: [showForAuthGuard],
      children: [
        {
          path: '/',
          name: 'Dashboard',
          component: () => import('@/views/Dashboard.vue'),
        },
        {
          path: '/jobs/new',
          beforeEnter: [showForAdminGuard],
          name: 'NewJob',
          component: () => import('@/views/job/NewJob.vue'),
        },
        {
          path: '/candidacies/:id',
          name: 'Candidacy',
          beforeEnter: [showForAdminGuard],
          component: () => import('@/views/CandidacyView.vue'),
        },
        {
          path: '/candidacies/job=:id',
          name: 'NewCandidacy',
          beforeEnter: [showForAdminGuard],
          component: () => import('@/views/NewCandidacyView.vue'),
        },
        {
          path: '/candidacies/:id/edit',
          beforeEnter: [showForAdminGuard],
          name: 'UpdateCandidacy',
          component: () => import('@/views/UpdateCandidacyView.vue'),
        },
        {
          path: '/candidates',
          name: 'CandidatesPage',
          component: () => import('@/views/CandidatesPage.vue'),
        },
        {
          path: '/candidacies',
          name: 'CandidaciesPage',
          component: () => import('@/views/CandidaciesPageView.vue'),
        },
        {
          path: '/users',
          beforeEnter: [showForAdminGuard],
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
          beforeEnter: [showForAdminGuard],
          name: 'UpdateJob',
          component: () => import('@/views/job/UpdateJob.vue'),
        },
        {
          path: '/settings',
          name: 'Settings',
          component: () => import('@/views/SettingsView.vue'),
        },
      ],
    },
    {
      path: '/login',
      name: 'Login',
      meta: {
        requiresAuth: false,
      },
      beforeEnter: [hideForAuthGuard],
      component: () => import('@/views/LoginView.vue'),
    },
    {
      path: '/signup',
      name: 'Signup',
      meta: {
        requiresAuth: false,
      },
      beforeEnter: [hideForAuthGuard],
      component: () => import('@/views/SignupView.vue'),
    },
    {
      path: '/forgot-password',
      name: 'ForgotPassword',
      meta: {
        requiresAuth: false,
      },
      beforeEnter: [hideForAuthGuard],
      component: () => import('@/views/ForgotPasswordView.vue'),
    },
    {
      path: '/password-reset/token=:token',
      name: 'PasswordReset',
      meta: {
        requiresAuth: false,
      },
      beforeEnter: [hideForAuthGuard],
      component: () => import('@/views/PasswordResetView.vue'),
    },
    {
      path: '/:catchAll(.*)',
      name: 'NotFound',
      meta: {
        requiresAuth: false,
      },
      component: () => import('@/views/NotFoundPage.vue'),
    },
  ],
});

// global guard

export default router;
