import { createRouter, createWebHistory } from 'vue-router'
import { message } from 'ant-design-vue'
import HomeView from '@/pages/HomePage.vue'
import LoginPage from '@/pages/LoginPage.vue'
import RegisterPage from '@/pages/RegisterPage.vue'
import UserManagePage from '@/pages/UserManagePage.vue'
import { useUserStore } from '@/stores/user'

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
    {
      path: '/admin/user',
      name: 'userManage',
      component: UserManagePage,
      meta: { requiresAdmin: true },
    },
  ],
})

// 路由守卫：检查 admin 权限
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  // 检查路由是否以 /admin 开头或有 requiresAdmin 标记
  const requiresAdmin = to.path.startsWith('/admin') || to.meta.requiresAdmin
  
  if (requiresAdmin) {
    // 检查用户是否登录
    if (!userStore.isLogin) {
      message.warning('请先登录')
      next('/login')
      return
    }
    
    // 检查用户是否是管理员
    if (userStore.userInfo?.userRole !== 'admin') {
      message.error('无权访问，需要管理员权限')
      next('/')
      return
    }
  }
  
  next()
})

export default router
