import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/pages/HomePage.vue'
import LoginPage from '@/pages/LoginPage.vue'
import RegisterPage from '@/pages/RegisterPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginPage,
      meta: { hideLayout: true },
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterPage,
      meta: { hideLayout: true },
    },
  ],
})

export default router
