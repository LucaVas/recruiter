import { createRouter, createWebHistory } from 'vue-router';
import { authenticate, showForAdmin } from './guards';
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
          component: () => import('../views/Dashboard.vue'),
        },
        {
          path: '/jobs/new',
          beforeEnter: [showForAdmin],
          name: 'NewJob',
          component: () => import('../views/new-job/NewJob.vue'),
        },
        {
          path: '/candidacies/job=:id',
          name: 'NewCandidacy',
          component: () => import('../views/new-candidacy/NewCandidacy.vue'),
        },
        {
          path: '/candidacies/:id',
          name: 'Candidacy',
          component: () => import('../views/candidacy/Candidacy.vue'),
        },
        {
          path: '/candidates',
          name: 'CandidatesPage',
          component: () => import('../views/candidates-page/CandidatesPage.vue'),
        },
        {
          path: '/candidacies',
          name: 'CandidaciesPage',
          component: () => import('../views/candidacies-page/CandidaciesPage.vue'),
        },
        {
          path: '/users',
          beforeEnter: [showForAdmin],
          name: 'UsersView',
          component: () => import('../views/UsersView.vue'),
        },
        {
          path: '/jobs/:id',
          name: 'Job',
          component: () => import('../views/job/Job.vue'),
        },
        {
          path: '/jobs/update/:id',
          name: 'UpdateJob',
          component: () => import('../views/update-job/UpdateJob.vue'),
        },
      ],
    },
    // {
    //   path: '/verify',
    //   name: 'Verify',
    //   beforeEnter: [hideForAuth],
    //   component: () => import('../views/VerifyTokenView.vue'),
    //   props: (route) => ({ email: route.query.email, token: route.query.token }),
    // },
    // {
    //   path: '/resetPassword',
    //   name: 'ResetPassword',
    //   beforeEnter: [hideForAuth],
    //   component: () => import('../views/ResetPasswordView.vue'),
    //   props: (route) => ({ email: route.query.email, token: route.query.token }),
    // },
    // {
    //   path: '/reset',
    //   name: 'Reset',
    //   beforeEnter: [hideForAuth],
    //   component: () => import('../views/SendResetPasswordLinkView.vue'),
    // },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/login/LoginView.vue'),
    },
    {
      path: '/signup',
      name: 'Signup',
      component: () => import('../views/signup/SignupView.vue'),
    },
    {
      path: '/:catchAll(.*)',
      name: 'NotFound',
      component: () => import('../views/not-found/NotFoundPage.vue'),
      meta: {
        requiresAuth: false,
      },
    },
  ],
});

export default router;
