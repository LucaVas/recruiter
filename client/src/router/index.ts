import { createRouter, createWebHistory } from 'vue-router';
import { authenticate, hideForAuth } from './guards';
import DashboardLayout from '@/layouts/DashboardLayout.vue';

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
          path: '/Jobs/New',
          name: 'NewJob',
          component: () => import('../views/NewJob.vue'),
        },
        //     // {
        //     //   path: '/playgrounds',
        //     //   name: 'Playgrounds',
        //     //   component: () => import('../views/PlaygroundsMapView.vue'),
        //     // },
        //     // {
        //     //   path: '/playgrounds/:id',
        //     //   name: 'Playground',
        //     //   component: () => import('../views/PlaygroundView.vue'),
        //     // },
        //     // {
        //     //   path: 'playgrounds/:id/report',
        //     //   name: 'PlaygroundReport',
        //     //   component: () => import('../views/NewReportView.vue'),
        //     // },
        //     // {
        //     //   path: '/report',
        //     //   name: 'NewReport',
        //     //   component: () => import('../views/NewReportView.vue'),
        //     // },
        //     // {
        //     //   path: 'reports/:id',
        //     //   name: 'Report',
        //     //   component: () => import('../views/ReportView.vue'),
        //     // },
        //     // {
        //     //   path: '/myReports',
        //     //   name: 'MyReports',
        //     //   beforeEnter: [showForVerified],
        //     //   component: () => import('../views/MyReportsView.vue'),
        //     // },
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
      beforeEnter: [hideForAuth],
      component: () => import('../views/LoginView.vue'),
    },
    {
      path: '/signup',
      name: 'Signup',
      beforeEnter: [hideForAuth],
      component: () => import('../views/SignupView.vue'),
    },
    {
      path: '/:catchAll(.*)',
      name: 'NotFound',
      component: () => import('../views/SignupView.vue'),
      meta: {
        requiresAuth: false,
      },
    },
  ],
});

export default router;
